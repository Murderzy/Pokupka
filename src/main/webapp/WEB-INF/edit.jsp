<%@ page import="org.data.entities.Goods" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %><%
    String userLogin = (String)request.getSession().getAttribute("login");
    String home = request.getContextPath() ;
    List<Goods> listbyuser = (List<Goods>) request.getAttribute("listbyuser");
%>
<%
    for(Goods g: listbyuser){%>

<form method="post" action="">
    <input type="hidden" value="<%=g.getId()%>" name="id"/>
    <ul>
        <li><%=g.getTitle()%>   <%=g.getDescription()%>   <%=g.getPrice()%>

            <img src="<%=home%>/image/<%=g.getImage()%>" />
        </li>

    </ul>
    <input type="submit" value="Edit"/>
</form>

<%  } %>