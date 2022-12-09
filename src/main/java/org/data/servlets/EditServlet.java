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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@WebServlet( "/edit" )
@Singleton
public class EditServlet extends HttpServlet {

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
        System.out.println("GET Update");
        req.getRequestDispatcher("/WEB-INF/edit.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        System.out.println("POST UPDATE");
        String id = req.getParameter("id");
        Goods item = goodsDAO.findGoodById(id);
        session.setAttribute("item",item);
        resp.sendRedirect("http://localhost:8080/Pokupka_war_exploded/editone");

    }
}