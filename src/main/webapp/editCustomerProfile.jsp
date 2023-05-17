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
    <title>Edit Profile - Customer</title>
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
<c:if test="${not empty getCustomer}">
    <section class="vh-100 gradient-custom">
        <div class="container py-5 h-100">
            <div class="row justify-content-center align-items-center h-100">
                <div class="col-12 col-lg-9 col-xl-7">
                    <div class="card shadow-2-strong card-registration rounded mb-3">
                        <div class="card-body p-4 p-md-5">
                            <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Edit Profile - Customer</h3>
                            <form class="row g-3 needs-validation" action="CustomerServlet?user=${user.userType}&action=editCustomer&customerId=${getCustomer.id}" method="post" novalidate >
                                <div class="col-md-6">
                                    <label for="editEmail" class="form-label">Email</label>
                                    <input type="email" name="email" class="form-control" id="editEmail" value="${user.email}" readonly>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please insert your email.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="editPassword" class="form-label">Password (Leave blank to keep old password)</label>
                                    <input type="password" name="password" class="form-control" id="editPassword">
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please insert your password.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="validationCustom01" class="form-label">Full Name</label>
                                    <input type="text" name="name" class="form-control" id="validationCustom01" value="${getCustomer.name}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please insert your name.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="validationCustomPhone" class="form-label">Phone Number</label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text" id="inputGroupPrepend">+60</span>
                                        <input type="tel" name="phone" class="form-control" id="validationCustomPhone" aria-describedby="inputGroupPrepend" pattern="\d{8,10}" minlength="8" maxlength="10" value="${getCustomer.phoneNumber}" required>
                                        <div class="valid-feedback">
                                            Looks good!
                                        </div>
                                        <div class="invalid-feedback">
                                            Please enter a valid phone number.(Do not include "-" in your phone number)
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <label for="street" class="form-label">House Number, Building, Street Name</label>
                                    <input type="text" name="street" class="form-control" id="street" value="${getCustomer.street}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please provide a valid address.
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <label for="state" class="form-label">State</label>
                                    <select class="form-select" name="state" id="state" required>
                                        <option selected value="${getCustomer.state}">${getCustomer.state}</option>
                                        <c:forEach var="state" items="<%=StaticVar.States%>">
                                            <option value="${state}">${state}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please select a valid state.
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <label for="city" class="form-label">City</label>
                                    <input type="text" name="city" class="form-control" id="city" value="${getCustomer.city}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please provide a valid city.
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <label for="postalCode" class="form-label">Postal Code</label>
                                    <input type="text" name="postalCode" class="form-control" id="postalCode" pattern="[0-9]{5}" maxlength="5" value="${getCustomer.postalCode}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please provide a valid postal code.
                                    </div>
                                </div>
                                <div class="col-12">
                                    <button class="btn btn-primary" type="submit">Submit form</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</c:if>
</body>
</html>
