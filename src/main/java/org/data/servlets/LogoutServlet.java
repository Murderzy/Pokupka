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

@WebServlet( "/logout" )
@Singleton
public class LogoutServlet extends HttpServlet {




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("userLogin");
        session.removeAttribute("login");

        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);

    }
}
