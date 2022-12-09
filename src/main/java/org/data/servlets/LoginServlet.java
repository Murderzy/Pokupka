package org.data.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.dao.UserDAO;
import org.data.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet( "/log" )
@Singleton
public class LoginServlet extends HttpServlet {
    //@Inject
    //private EmailService emailService ;

    @Inject
    private UserDAO userDAO ;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession() ;

        System.out.println(session.getAttribute("userLogin"));

        req.getRequestDispatcher( "WEB-INF/login.jsp" ).forward( req, resp ) ;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession() ;

        // Прием данных от формы регистрации
        String userLogin = req.getParameter( "userLogin" ) ;
        String userPassword = req.getParameter( "userPassword" ) ;

        User user =userDAO.getUserByCredentials(userLogin,userPassword);

        if(user != null) {
            session.setAttribute("userLogin", userLogin);

            System.out.println(session.getAttribute("userLogin"));


            resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/");
        }
        else
        {
            req.getRequestDispatcher( "WEB-INF/login.jsp" ).forward( req, resp ) ;
        }
    }
}
