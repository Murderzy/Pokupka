package org.data.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.dao.GoodsDAO;
import org.data.entities.Goods;
import org.data.services.MimeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@WebServlet( "/editone" )
@MultipartConfig
@Singleton
public class EditOneServlet extends HttpServlet {

    @Inject
    private MimeService mimeService;
    @Inject
    private GoodsDAO goodsDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Get UpdateOne");
        HttpSession session = req.getSession();
        Goods g = (Goods)session.getAttribute("item");
        System.out.println(g.getTitle());
        session.setAttribute("item",g);
        req.getRequestDispatcher("/WEB-INF/editone.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Post UpdateOne");
        HttpSession session = req.getSession();

        // Прием данных от формы регистрации
        String goodTitle = req.getParameter("dataTitleEdit");
        String goodDescription = req.getParameter("dataDescriptionEdit");
        String goodPrice = req.getParameter("dataPriceEdit");
        Part goodImage = req.getPart("dataImageEdit");  // часть, отвечающая за файл (имя - как у input)
        String goodId = req.getParameter("dataIdEdit");

        System.out.println(goodTitle);  //  null!!!!!

        System.out.println("data --- "+ req.getParameter("dataTitle"));

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
//                // сохраняем
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
            goods.setId(goodId);
            goods.setUser_id((String)req.getSession().getAttribute("login"));
            System.out.println(goods.getTitle()+ " "+ goods.getDescription() + " "+ goods.getPrice()+" "+goods.getImage()+" "+ goods.getUser_id());
            goodsDAO.updateGoodsById(goods.getId(), goods);
        }
        catch( Exception ex ) {
            errorMessage = ex.getMessage() ;
        }

        // Проверяем успешность валидации
        if( errorMessage != null ) {  // Есть ошибки
            session.setAttribute( "regErr", errorMessage ) ;
            resp.sendRedirect( req.getRequestURI() );
        }
        else {
            session.setAttribute( "reg", "Registration successful" ) ;
            resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/edit");
        }

        //resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/");

        //resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/edit");

    }
}