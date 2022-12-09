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
import java.sql.Date;
import java.util.UUID;

@WebServlet( "/goods/" )
@MultipartConfig
@Singleton
public class GoodsServlet extends HttpServlet {
    //@Inject
    //private EmailService emailService ;
    @Inject
    private GoodsDAO goodsDAO;
    @Inject
    private MimeService mimeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Проверяем, есть ли сохраненные в сессии данные от предыдущей обработки
        HttpSession session = req.getSession();
        //System.out.println(req.getSession().getAttribute("userLogin"));
        String regError = (String) session.getAttribute("regError");
        String regOk = (String) session.getAttribute("regOk");
        if (regError != null) {  // Есть сообщение об ошибке
            req.setAttribute("regError", regError);
            session.removeAttribute("regError");  // удаляем сообщение из сессии
        }
        if (regOk != null) {  // Есть сообщение об успешной регистрации
            req.setAttribute("regOk", regOk);
            session.removeAttribute("regOk");  // удаляем сообщение из сессии
        }


        //req.setAttribute( "pageBody", "reg_user.jsp" ) ;
        req.getRequestDispatcher("/WEB-INF/goods.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Прием данных от формы регистрации
        String goodTitle = req.getParameter("dataTitle");
        String goodDescription = req.getParameter("dataDescription");
        String goodPrice = req.getParameter("dataPrice");
        Part goodImage = req.getPart("dataImage");  // часть, отвечающая за файл (имя - как у input)


        System.out.println(goodTitle);

        String errorMessage = null ;
        try {
            if( goodTitle == null || goodTitle.isEmpty() ) {
                throw new Exception( "Title could not be empty" ) ;
            }

            if( goodDescription == null || goodDescription.isEmpty() ) {

                throw new Exception( "Description could not be empty" ) ;
            }

            if( goodPrice == null ) {
                throw new Exception( "Price could not be empty" ) ;
            }



            // region Avatar
            if( goodImage == null ) {  // такое возможно если на форме нет <input type="file" name="userAvatar"
                throw new Exception( "Form integrity violation" ) ;
            }
            long size = goodImage.getSize() ;
            String savedName = null ;
            if( size > 0 ) {  // если на форме есть input, то узнать приложен ли файл можно по его размеру
                // файл приложен - обрабатываем его
                String userFilename = goodImage.getSubmittedFileName() ;  // имя приложенного файла
                // отделяем расширение, проверяем на разрешенные, имя заменяем на UUID
                int dotPosition = userFilename.lastIndexOf( '.' ) ;
                if( dotPosition == -1 ) {
                    throw new Exception( "File extension required" ) ;
                }
                String extension = userFilename.substring( dotPosition ) ;
                if( ! mimeService.isImage( extension ) ) {
                    throw new Exception( "File type unsupported" ) ;
                }

               savedName = UUID.randomUUID() + extension ;
                // сохраняем
                // String path = new File( "./" ).getAbsolutePath() ;  // запрос текущей директории - C:\xampp\tomcat\bin\.
                String path = req.getServletContext().getRealPath( "/" ) ;  // ....\target\WebBasics\
                File file = new File( path + "../upload/" + savedName ) ;
                Files.copy( goodImage.getInputStream(), file.toPath() ) ;
            }
            // endregion


            Goods goods = new Goods();
            goods.setTitle(goodTitle);
            goods.setDescription(goodDescription);
            goods.setPrice(Float.parseFloat(goodPrice));
            goods.setImage(savedName);
            //goods.setDate(getDay());
            goods.setUser_id((String)req.getSession().getAttribute("login"));
            System.out.println(goods.getTitle()+ " "+ goods.getDescription() + " "+ goods.getPrice()+" "+goods.getImage()+" "+ goods.getUser_id());
            if( goodsDAO.add( goods ) == null ) {
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
            session.setAttribute( "regOk", "Registration successful" ) ;
        }

        resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/");

    }
}