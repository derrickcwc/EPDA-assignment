<%--
  Created by IntelliJ IDEA.
  User: derri
  Date: 21/4/2023
  Time: 11:42 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.shopoo.StaticVar" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add New Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/newCustomer.css">
    <script src="js/newCustomer.js"></script>
</head>
<body>
<%@ include file="Utils/sellerNav.jsp" %>
<c:if test="${empty user}">
    <%
                response.sendRedirect(request.getContextPath() + "/login.jsp");
    %>
</c:if>
<c:if test="${user.userType ne 'Multi' && user.userType ne 'Seller'}">
    <%
        response.sendRedirect(request.getContextPath() + "/newSeller.jsp");
    %>
</c:if>
<c:if test="${not empty errorMsg}">
    <%-- call the showErrorModal() function with the error message --%>
    <%@include file="Utils/errorMessage.jsp"%>
    <script>
        <%@include file="js/ManageAdmin.js"%>
    </script>
    <%
        request.getSession().removeAttribute("errorMsg");
    %>
</c:if>
<section class="vh-100 gradient-custom">
    <div class="container py-5 h-100">
        <div class="row justify-content-center align-items-center h-100">
            <div class="col-12 col-lg-9 col-xl-7">
                <div class="card shadow-2-strong card-registration rounded">
                    <div class="card-body p-4 p-md-5">
                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Add New Product</h3>
                        <form class="row g-3 needs-validation" action="ProductServlet" method="post" enctype="multipart/form-data" novalidate >
                            <div class="col-md-6">
                                <label for="validationCustom01" class="form-label">Product Name</label>
                                <input type="text" name="productName" class="form-control" id="validationCustom01" required>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please insert your Product name.
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="price" class="form-label">Price</label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text" id="inputGroupPrepend">RM</span>
                                    <input type="number" id="price" name="price" min="0" max="999999" step="0.01" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please enter a valid Price.
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <label for="formFile" class="form-label">Product Image</label>
                                <input class="form-control" type="file" name="productImage" accept="image/*" id="formFile">
                            </div>
                            <div class="col-12">
                                <label for="floatingTextarea" class="form-label">Product Description</label>
                                <div class="form-floating">
                                    <textarea class="form-control" name="description" placeholder="Describe your shop" id="floatingTextarea" maxlength="150" style="height: 100px"></textarea>
                                    <label for="floatingTextarea">Describe your product here... (200 characters)</label>
                                </div>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please describe your product.
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" value="" id="invalidCheck" required>
                                    <label class="form-check-label" for="invalidCheck">
                                        Agree to terms and conditions
                                    </label>
                                    <div class="invalid-feedback">
                                        You must agree before submitting.
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <button class="btn btn-primary" type="submit">Add Product</button>
                            </div>
                            <input type="hidden" name="action" value="addProduct">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>
