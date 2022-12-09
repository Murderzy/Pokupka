<jsp:include page="/WEB-INF/headerfragment.jsp" />

<div class="card-body py-md-4">
  <form method="post" action="">
    <div class="form-group">
      <input type="text"     class="form-control" name="userLogin"       placeholder="Login" /><br/>
      <input type="password" class="form-control" name="userPassword"    placeholder="Password" /><br/>
    </div>
    <br/>
    <div class="d-flex flex-row align-items-center justify-content-between">
      <button type="submit" class="btn btn-primary">Sign In</button>
    </div>
  </form>
</div>
<jsp:include page="/WEB-INF/footer.jsp" />
