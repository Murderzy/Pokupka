<%@ page import="org.data.entities.Goods" %>
<%@ page import="java.util.List" %>
<%@ page import="org.data.entities.User" %>
<%@ page import="org.data.dao.GoodsDAO" %>
<jsp:include page="/WEB-INF/headerfragment.jsp" />

<%@ page contentType="text/html;charset=UTF-8" %><%
    String userLogin = (String)request.getSession().getAttribute("login");
    User u = (User)request.getSession().getAttribute("ImageUser");
    String home = request.getContextPath() ;
    List<Goods> listbyuser = (List<Goods>) request.getAttribute("listbyuser");

    List<Goods> listall = (List<Goods>) request.getAttribute("listall"); ;
%>


<h2>List ALL</h2>

<%
    for(Goods g: listall){%>

<ul>
    <li><%=g.getTitle()%>   <%=g.getDescription()%>   <%=g.getPrice()%> <%=g.getUser_id()%>

        <img src="<%=home%>/image/<%=g.getImage()%>" />
    </li>

</ul>

<%  } %>



<h1>Main</h1>
<% if(userLogin == null) {%>
<form method="get" action="<%=home%>/log">
    <button type="submit">Sign In</button>
</form>

<form method="get" action="<%=home%>/register/">
    <button type="submit">Sign Up</button>
</form>
<% }else{ %>
<div>Login</div>
<img src="<%=home%>/image/<%=u.getAvatar()%>" />
<h1>Hello <%=userLogin%></h1>
<form method="get" action="<%=home%>/logout">
    <input type="submit" value="logout"/>
</form>
<form method="get" action="<%=home%>/settings">
    <input type="submit" value="Settings"/>
</form>
<br/><br/><br/>
<div>
    <form method="get" action="<%=home%>/goods/">
        <button type="submit">Add</button>
    </form>
</div>
<h2>List USERS </h2>
<form method="get" action="<%=home%>/edit">
    <button type="submit">Edit</button>
</form>
<form method="get" action="<%=home%>/delete">
    <button type="submit">Delete</button>
</form>
<%
    for(Goods g: listbyuser){%>

        <ul>
            <li><%=g.getTitle()%>   <%=g.getDescription()%>   <%=g.getPrice()%>

                <img src="<%=home%>/image/<%=g.getImage()%>" />
            </li>

        </ul>

  <%  } %>
<%}%>
</body>

</html>