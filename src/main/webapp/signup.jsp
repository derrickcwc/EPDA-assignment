<%--
  Created by IntelliJ IDEA.
  User: derri
  Date: 16/4/2023
  Time: 9:09 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- check if error message attribute is set --%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Shopoo - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <%@ include file="Utils/loginNav.jsp" %>
    <link rel="stylesheet" href="css/signup.css">
    <%--    <script src="js/login.js"></script>--%>
    <%-- check if error message attribute is set --%>
    <c:if test="${not empty errorMsg}">
        <%-- call the showErrorModal() function with the error message --%>
        <%@include file="Utils/errorMessage.jsp"%>
        <script>
            <%@include file="js/signup.js"%>
        </script>
        <%
            request.getSession().removeAttribute("errorMsg");
        %>
    </c:if>
</head>
<body>
<div class="section">
    <div class="container">
        <div class="row pt-5">
            <div class="col-4 offset-4 text-center">
                <h1>
                    Shopoo
                    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-shopping-cart" width="44" height="44" viewBox="0 0 24 24" stroke-width="1.5" stroke="#c4c3ca" fill="none" stroke-linecap="round" stroke-linejoin="round">
                        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                        <circle cx="6" cy="19" r="2" />
                        <circle cx="17" cy="19" r="2" />
                        <path d="M17 17h-11v-14h-2" />
                        <path d="M6 5l14 1l-1 7h-13" />
                    </svg>
                </h1>
            </div>
        </div>
        <div class="row justify-content-center">
            <div class="col-12 text-center">
                <div class="section pb-5 pt-5 pt-sm-2 text-center">
                    <div class="card-wrap mx-auto">
                        <div class ="card-wrapper h-75">
                            <div class="section text-center">
                                <h4 class="my-4 pb-3">Sign Up</h4>
                                <form action="${pageContext.request.contextPath}/user-servlet" method="post">
                                    <div class="center-wrap">
                                        <div class="input-group mb-3">
                                            <span class="input-group-text bg-secondary w-25" id="loginEmail">Email</span>
                                            <input type="email" class="form-control" name="email" aria-describedby="inputGroup-sizing-default" required>
                                        </div>
                                        <div class="input-group mb-3">
                                            <span class="input-group-text bg-secondary w-25" id="loginPassword">Password</span>
                                            <input type="password" class="form-control" name="password" aria-describedby="inputGroup-sizing-default" required>
                                        </div>
                                        <input type="hidden" name="action" value="register" readonly>
                                        <button class="btn btn-secondary mb-3" type="submit" data-toggle="modal" data-target=".modal">Sign up</button>
                                        <p>Already have an account?
                                            <a href="login.jsp">Login</a>
                                        </p>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>