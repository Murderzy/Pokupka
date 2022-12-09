<jsp:include page="/WEB-INF/headerfragment.jsp" />

<%@ page import="org.data.entities.Goods" %>
<%@ page contentType="text/html;charset=UTF-8" %><%
  String home = request.getContextPath() ;
  String regError = (String) request.getAttribute( "regErr" ) ;
  String regOk = (String) request.getAttribute( "reg" ) ;
  Goods g= (Goods)request.getSession().getAttribute("item");

%>

<div class="container"><div class="row justify-content-center"><div class="col-md-5"><div class="card">
  <h2 class="card-title text-center">Edit</h2>
  <% if( regError != null ) { %><h3 class="card-title text-center reg-error"><%=regError%></h3><% } %>
  <% if( regOk != null ) { %><h3 class="card-title text-center reg-ok"><%=regOk%></h3><% } %>
  <div class="card-body py-md-4">
    <form method="post" action="<%=home%>/editone" enctype="multipart/form-data">
      <div class="form-group">
        <input type="text"     class="form-control" name="dataTitleEdit"    value="<%=g.getTitle()%>"   placeholder="Title" /><br/>
        <input type="text"     class="form-control" name="dataDescriptionEdit"      value="<%=g.getDescription()%>"   placeholder="Description" /><br/>
        <input type="text"     class="form-control" name="dataPriceEdit"    value="<%=g.getPrice()%>"     placeholder="Price" /><br/>
        <input type="file"     class="form-control" value="<%=g.getImage()%>"  name="dataImageEdit" />
        <input type="hidden" value="<%=g.getId()%>" name="dataIdEdit"/>
      </div>
      <br/>
      <div class="d-flex flex-row align-items-center justify-content-between">
        <button type="submit" class="btn btn-primary">Edit</button>
      </div>
    </form>
  </div>
</div></div></div></div>

<jsp:include page="/WEB-INF/footer.jsp" />