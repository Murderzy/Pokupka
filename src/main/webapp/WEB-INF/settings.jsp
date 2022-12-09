<jsp:include page="/WEB-INF/headerfragment.jsp" />

<%@ page import="org.data.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" %><%
    String home = request.getContextPath() ;
    String regError = (String) request.getAttribute( "regError" ) ;
    String regOk = (String) request.getAttribute( "regOk" ) ;
    User u = (User)request.getSession().getAttribute("user");

%>

<div class="container"><div class="row justify-content-center"><div class="col-md-5"><div class="card">
    <h2 class="card-title text-center">Update Account</h2>
    <% if( regError != null ) { %><h3 class="card-title text-center reg-error"><%=regError%></h3><% } %>
    <% if( regOk != null ) { %><h3 class="card-title text-center reg-ok"><%=regOk%></h3><% } %>
    <div class="card-body py-md-4">
        <form method="post" action="<%=home%>/settings" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text"     class="form-control" name="userLogin"       value="<%=u.getLogin()%>" /><br/>
                <input type="text"     class="form-control" name="userName"        value="<%=u.getName()%>" /><br/>
                <input type="text"     class="form-control" name="userSurname"     value="<%=u.getSurname()%>" /><br/>
                <input type="file"     class="form-control" name="userAvatar"  value="<%=u.getAvatar()%>"/>
                <input type="email"     class="form-control" name="userEmail"  value="<%=u.getEmail()%>"/>
                <input type="text"     class="form-control" name="userPhone" value="<%=u.getPhone()%>"/>
                <input type="hidden" value="<%=u.getId()%>" name="userId"/>
                <input type="hidden" value="<%=u.getSalt()%>" name="userSalt"/>
                <input type="hidden" value="<%=u.getPass()%>" name="userPassword"/>
            </div>
            <br/>
            <div class="d-flex flex-row align-items-center justify-content-between">
                <button type="submit" class="btn btn-primary">Update Account</button>
            </div>
        </form>
    </div>
</div></div></div></div>

<jsp:include page="/WEB-INF/footer.jsp" />
