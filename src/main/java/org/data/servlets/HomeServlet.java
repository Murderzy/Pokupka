package org.data.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.dao.GoodsDAO;
import org.data.dao.UserDAO;
import org.data.entities.Goods;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet( "/" )
@Singleton
public class HomeServlet extends HttpServlet {
    //@Inject
    //private EmailService emailService ;

    @Inject
    private GoodsDAO goodsDAO;
    @Inject
    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = (String)session.getAttribute("userLogin" );
        session.setAttribute("login",login);
        if(login != null) {
            session.setAttribute("ImageUser", userDAO.getUserByLogin(login));
        }
        else
        {
            session.removeAttribute("ImageUser");
        }
        List<Goods> listbyuser = new LinkedList<Goods>();
        listbyuser= goodsDAO.getAllGoodsByUser(login);
        session.setAttribute("listbyuser",listbyuser);
        List<Goods> listall = new LinkedList<Goods>();
        listall= goodsDAO.getAllGoods();
        session.setAttribute("listall",listall);
        req.getRequestDispatcher( "WEB-INF/index.jsp" ).forward( req, resp ) ;

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        goodsDAO.deleteGoodsById(id);
        resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/");
    }
}
