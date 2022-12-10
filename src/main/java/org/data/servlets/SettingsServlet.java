package org.data.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.dao.GoodsDAO;
import org.data.dao.UserDAO;
import org.data.entities.Goods;
import org.data.entities.User;
import org.data.services.MimeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@WebServlet( "/settings" )
@MultipartConfig
@Singleton
public class SettingsServlet extends HttpServlet {

    @Inject
    private MimeService mimeService;
    @Inject
    private UserDAO userDAO;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = (String)session.getAttribute("userLogin" );

        System.out.println(login);

        User user = userDAO.getUserByLogin(login);

        session.setAttribute("user",user);

        req.getRequestDispatcher("/WEB-INF/settings.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession() ;

        // Прием данных от формы регистрации
        String userId = req.getParameter("userId");
        String userSalt = req.getParameter("userSalt");
        String userLogin = req.getParameter( "userLogin" );
        String userPassword = req.getParameter( "userPassword" ) ;
        String userName = req.getParameter( "userName" ) ;
        String userSurname = req.getParameter( "userSurname" ) ;
        String userEmail = req.getParameter( "userEmail" ) ;
        String userPhone = req.getParameter( "userPhone" ) ;
        Part userAvatar = req.getPart( "userAvatar" ) ;  // часть, отвечающая за файл (имя - как у input)


        session.setAttribute("userName", userName);

        System.out.println(userLogin);

        // Валидация данных
        String errorMessage = null ;
        try {
            if( userLogin == null || userLogin.isEmpty() ) {
                throw new Exception( "Login could not be empty" ) ;
            }
            if( ! userLogin.equals( userLogin.trim() ) ) {
                throw new Exception( "Login could not contain trailing spaces" ) ;
            }

            if( userPassword == null || userPassword.isEmpty() ) {
                session.setAttribute("userLogin", userLogin);
                throw new Exception( "Password could not be empty" ) ;
            }

            if( userName == null || userName.isEmpty() ) {
                throw new Exception( "Name could not be empty" ) ;
            }
            if( ! userName.equals( userName.trim() ) ) {
                throw new Exception( "Name could not contain trailing spaces" ) ;
            }


            // region Avatar
            if( userAvatar == null ) {  // такое возможно если на форме нет <input type="file" name="userAvatar"
                throw new Exception( "Form integrity violation" ) ;
            }
            long size = userAvatar.getSize() ;
            String savedName = null ;
            if( size > 0 ) {  // если на форме есть input, то узнать приложен ли файл можно по его размеру
                // файл приложен - обрабатываем его
                String userFilename = userAvatar.getSubmittedFileName() ;  // имя приложенного файла
                // отделяем расширение, проверяем на разрешенные, имя заменяем на UUID
                int dotPosition = userFilename.lastIndexOf( '.' ) ;
                if( dotPosition == -1 ) {
                    throw new Exception( "File extension required" ) ;
                }
                String extension = userFilename.substring( dotPosition ) ;
                if( ! mimeService.isImage( extension ) ) {
                    throw new Exception( "File type unsupported" ) ;
                }

                //??????
               savedName = UUID.randomUUID() + extension ;
//                // сохраняем
                // String path = new File( "./" ).getAbsolutePath() ;  // запрос текущей директории - C:\xampp\tomcat\bin\.
                String path = req.getServletContext().getRealPath( "/" ) ;  // ....\target\WebBasics\
                File file = new File( path + "../upload/" + savedName ) ;
                Files.copy( userAvatar.getInputStream(), file.toPath() ) ;
            }
            // endregion


            User user = new User() ;
            user.setName( userName ) ;
            user.setLogin( userLogin ) ;
            user.setPass( userPassword ) ;
            user.setAvatar( savedName ) ;
            user.setSurname(userSurname);
            user.setEmail(userEmail);
            user.setPhone(userPhone);
            user.setId(userId);
            user.setSalt(userSalt);
            if( userDAO.updateUser( user ) == 0 ) {
                throw new Exception( "Server error, try later" ) ;
            }
        }
        catch( Exception ex ) {
            errorMessage = ex.getMessage() ;
        }
        // Проверяем успешность валидации
        if( errorMessage != null ) {  // Есть ошибки
            session.setAttribute( "regError", errorMessage ) ;
        }
        else {
            session.setAttribute( "regOk", "Update successful" ) ;
        }

        resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/");

    }
}
