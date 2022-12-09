package org.data.dao;

import org.data.entities.Goods;
import org.data.services.DataInterface;

import javax.inject.Inject;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GoodsDAO {
    private final Connection connection ;
    private final DataInterface dataService ;

    @Inject
    public GoodsDAO( DataInterface dataService ) {
        this.dataService = dataService ;

        this.connection = dataService.getConnection() ;
    }


    public String add(Goods goods) {
        // генерируем id для новой записи
       String id = UUID.randomUUID().toString() ;

       // готовим запрос (подстановка введенных данных!!)
        String sql = "INSERT INTO Goods(`id`,`price`,`title`,`description`,`image`,`user`) VALUES(?,?,?,?,?,?)" ;
           try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.setFloat( 2, goods.getPrice() ); ;
            prep.setString(3,goods.getTitle());
            prep.setString( 4, goods.getDescription() ) ;
            prep.setString(5,goods.getImage());
            prep.setString( 6, goods.getUser_id() ) ;
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }
        System.out.println(id.toString() + " ---- add");
        return id ;
        //return null;
    }

    public List<Goods> getAllGoods() {

        List<Goods> list = new LinkedList<Goods>();
        String sql = "SELECT * from Goods" ;

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {

            ResultSet res = prep.executeQuery() ;
            while(res.next())
            {
                Goods goods = new Goods();
                goods.setId(res.getString("id"));
                goods.setPrice(res.getFloat("price"));
                goods.setTitle(res.getString("title"));
                goods.setDescription(res.getString("description"));
                goods.setImage(res.getString("image"));
                goods.setUser_id(res.getString("user"));
                System.out.println("Post" + goods.getId());
                list.add(goods);
            }
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
            return list;
        }

    public List<Goods> getAllGoodsByUser(String user) {

        List<Goods> list = new LinkedList<Goods>();
        String sql = "SELECT * from Goods Where `user`=?" ;

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, user ) ;
            ResultSet res = prep.executeQuery() ;
            while(res.next())
            {
                Goods goods = new Goods();
                goods.setId(res.getString("id"));
                goods.setPrice(res.getFloat("price"));
                goods.setTitle(res.getString("title"));
                goods.setDescription(res.getString("description"));
                goods.setImage(res.getString("image"));
                goods.setUser_id(res.getString("user"));
                System.out.println("Post" + goods.getId());
                list.add(goods);
            }
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return list;
    }

    public void deleteGoodsById(String id) {


        String sql = "DELETE FROM goods WHERE id = ?";

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            prep.executeUpdate() ;
            System.out.println("Delete");
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }

    }

    public Goods findGoodById(String id)
    {
        String sql = "SELECT * FROM goods WHERE id = ?";

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString( 1, id ) ;
            ResultSet res = prep.executeQuery() ;
            if(res.next())
            {
                Goods g = new Goods();
                g.setId(id);
                g.setUser_id(res.getString("user"));
                g.setTitle(res.getString("title"));
                g.setDescription(res.getString("description"));
                g.setPrice(res.getFloat("price"));
                g.setImage(res.getString("image"));
                return g;
            }
            System.out.println("Update");
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
        }
        return null;
    }

    public int updateGoodsById(String id,Goods g)
    {
        System.out.println("Update before try");
        String sql = "UPDATE goods SET price = ?, title = ?, description = ?,image =?,user=? WHERE id = ?";
        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            System.out.println("Update  try start");
            prep.setFloat( 1, g.getPrice() );
            prep.setString(2,g.getTitle());
            prep.setString(3,g.getDescription());
            prep.setString(4,g.getImage());
            prep.setString(5,g.getUser_id());
            prep.setString(6,g.getId());


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
}


