<%--
  Created by IntelliJ IDEA.
  User: derri
  Date: 13/5/2023
  Time: 11:11 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Top Up Balance - Customer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/newCustomer.css">
    <script src="js/newCustomer.js"></script>
</head>
<body>
<%@ include file="Utils/navbar.jsp" %>
<c:if test="${empty user}">
    <%
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    %>
</c:if>
<c:if test="${user.userType ne 'Multi' && user.userType ne 'Customer'}">
    <%
        response.sendRedirect(request.getContextPath() + "/newCustomer.jsp");
    %>
</c:if>
<c:if test="${not empty errorMsg}">
    <%-- call the showErrorModal() function with the error message --%>
    <%@include file="Utils/errorMessage.jsp"%>
<script>
    <%@include file="js/errorMessage.js"%>
</script>
        <%
        request.getSession().removeAttribute("errorMsg");
    %>
</c:if>
<body>
<div class="container">
    <div class="row mt-3">
        <div class="col-12">
            <div class="card rounded">
                <div class="card-header">
                    <div class="row">
                        <div class="col-4 my-auto">
                            <h6 class="m-0 font-weight-bold text-primary">Top-up Balance</h6>
                        </div>
                        <div class="col-4 offset-4">
                            <h6 class="m-0 font-weight-bold text-primary float-end" >Balance: RM <fmt:formatNumber value="${customerBalance}" pattern="0.00"/></h6>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="col-md-6">
                        <form action="TopUpBalance" method="post">
                            <label for="amount" class="form-label">Amount</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text" id="inputGroupPrepend">RM</span>
                                <input type="number" id="amount" name="amount" min="0" max="999999" step="0.01" required>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please enter a valid Amount.
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Top-Up</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
