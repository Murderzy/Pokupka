<jsp:include page="/WEB-INF/headerfragment.jsp" />

<%@ page import="org.data.entities.Goods" %>
<%@ page contentType="text/html;charset=UTF-8" %><%
  String home = request.getContextPath() ;
  String regError = (String) request.getAttribute( "regError" ) ;
 String regOk = (String) request.getAttribute( "regOk" ) ;


%>

<div class="container"><div class="row justify-content-center"><div class="col-md-5"><div class="card">
  <h2 class="card-title text-center">Register</h2>
  <% if( regError != null ) { %><h3 class="card-title text-center reg-error"><%=regError%></h3><% } %>
  <% if( regOk != null ) { %><h3 class="card-title text-center reg-ok"><%=regOk%></h3><% } %>
  <div class="card-body py-md-4">
    <form method="post" action="" enctype="multipart/form-data">
      <div class="form-group">
        <input type="text"     class="form-control" name="dataTitle"       placeholder="Title" /><br/>
        <input type="text"     class="form-control" name="dataDescription"        placeholder="Description" /><br/>
        <input type="text"     class="form-control" name="dataPrice"        placeholder="Price" /><br/>
        <input type="file"     class="form-control" name="dataImage" />

      </div>
      <br/>
      <div class="d-flex flex-row align-items-center justify-content-between">
        <button type="submit" class="btn btn-primary">Add</button>
      </div>
    </form>
  </div>
</div></div></div></div>

<jsp:include page="/WEB-INF/footer.jsp" />