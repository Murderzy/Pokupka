package org.data.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.data.dao.GoodsDAO;
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

@WebServlet("/delete")
@Singleton
public class DeleteServlet extends HttpServlet {
    //@Inject
    //private EmailService emailService ;

    @Inject
    private GoodsDAO goodsDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = (String)session.getAttribute("userLogin" );
        session.setAttribute("login",login);
        List<Goods> listbyuser = new LinkedList<Goods>();
        listbyuser= goodsDAO.getAllGoodsByUser(login);
        req.setAttribute("listbyuser",listbyuser);
        System.out.println("GET DELETe");
        req.getRequestDispatcher("/WEB-INF/delete.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("POST DELETe");
        String id = req.getParameter("id");
        goodsDAO.deleteGoodsById(id);
        resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/delete");

    }

}

