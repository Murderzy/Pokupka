package org.data.dao;

import org.data.entities.User;
import org.data.services.DataInterface;
import org.data.services.hash.HashService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class UserDAO {
    private final Connection connection ;
    private final HashService hashService ;
    private final DataInterface dataService ;
    @Inject
    public UserDAO( DataInterface dataService, HashService hashService ) {
        this.dataService = dataService ;
        this.hashService = hashService ;

        this.connection = dataService.getConnection() ;
    }

    public boolean confirmEmail( User user ) {
        if( user.getId() == null ) return false ;
        String sql = "UPDATE Users SET email_code = NULL WHERE id = ?";
        try( PreparedStatement prep = dataService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, user.getId() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( "UserDAO::confirmEmail " + ex.getMessage() ) ;
            System.out.println( sql + " " + user.getId() ) ;
            return false;
        }
        return true;
    }

    public int updateUser( User user ) {
        System.out.println("Update before try");
        String sql = "UPDATE users SET login = ?, pass = ?, name = ?,surname =?,salt=?,phone=?,email=?,avatar=? WHERE id = ?";
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            System.out.println("Update  try start");
            prep.setString( 1, user.getLogin() );
            prep.setString( 2, user.getPass() );
            prep.setString( 3, user.getName() );
            prep.setString( 4, user.getSurname() );
            prep.setString( 5, user.getSalt() );
            prep.setString( 6, user.getPhone() );
            prep.setString( 7, user.getEmail() );
            prep.setString( 8, user.getAvatar() );
            prep.setString( 9, user.getId() );


            System.out.println("Update  try end");
            System.out.println("Update");
            return prep.executeUpdate();
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        System.out.println("Update return 0");
        return 0;

    }

    public User getUserById( String userId ) {
        String sql = "SELECT * FROM Users u WHERE u.`id` = ? " ;
        try( PreparedStatement prep =
                     dataService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, userId ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                return new User( res ) ;
            }
        }
        catch( Exception ex ) {
            System.out.println( "UserDAO::getUserById " + ex.getMessage()
                    + "\n" + sql + " -- " + userId ) ;
        }
        return null ;
    }

    public User getUserByLogin( String userLogin ) {
        String sql = "SELECT * FROM Users u WHERE u.`login` = ? " ;
        try( PreparedStatement prep =
                     dataService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, userLogin ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                return new User( res ) ;
            }
        }
        catch( Exception ex ) {
            System.out.println( "UserDAO::getUserById " + ex.getMessage()
                    + "\n" + sql + " -- " + userLogin ) ;
        }
        return null ;
    }

    public String add( User user ) {
        System.out.println("Add User");
        // генерируем id для новой записи
        String id = UUID.randomUUID().toString() ;
        // генерируем соль
        String salt = hashService.hash( UUID.randomUUID().toString() ) ;
        // генерируем хеш пароля
        String passHash = this.hashPassword( user.getPass(), salt ) ;
        // готовим запрос (подстановка введенных данных!!)
        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`,`surname`,`salt`,`phone`,`email`,`avatar`) VALUES(?,?,?,?,?,?,?,?,?)" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.setString( 2, user.getLogin() ) ;
            prep.setString( 3, passHash ) ;
            prep.setString( 4, user.getName() ) ;
            prep.setString(5,user.getSurname());
            prep.setString( 6, salt ) ;
            prep.setString(7,user.getPhone());
            prep.setString(8,user.getEmail());
            prep.setString( 9, user.getAvatar() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }
        return id ;
    }

    public boolean isLoginUsed( String login ) {
        String sql = "SELECT COUNT(u.`id`) FROM Users u WHERE u.`login`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            ResultSet res = prep.executeQuery() ;
            res.next() ;
            return res.getInt(1) > 0 ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
            return true ;
        }
    }


    public String hashPassword( String password, String salt ) {
        return hashService.hash( salt + password + salt ) ;
    }

    public User getUserByCredentials( String login, String pass ) {
        String sql = "SELECT u.* FROM Users u WHERE u.`login`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                User user = new User( res ) ;
                // pass - открытый пароль, user.pass - Hash(pass,user.salt)
                String expectedHash = this.hashPassword( pass, user.getSalt() ) ;
                if( expectedHash.equals( user.getPass() ) ) {
                    return user ;
                }
            }
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return null ;
    }
    public User getUserByCredentialsOld( String login, String pass ) {
        String sql = "SELECT u.* FROM Users u WHERE u.`login`=? AND u.`pass`=?" ;
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, login ) ;
            prep.setString( 2, this.hashPassword( pass, "" ) ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) return new User( res ) ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return null ;
    }
}
