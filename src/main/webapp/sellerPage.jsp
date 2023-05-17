<%--
  Created by IntelliJ IDEA.
  User: derri
  Date: 21/4/2023
  Time: 11:42 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.example.shopoo.StaticVar" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.shopoo.Model.UserLog" %>
<%@ page import="com.example.shopoo.Model.Seller" %>
<%@ page import="java.text.DecimalFormat" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Seller Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/sellerPage.css">
    <script src="js/sellerPage.js"></script>
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
        <%@include file="js/errorMessage.js"%>
    </script>
    <%
        request.getSession().removeAttribute("errorMsg");
    %>
</c:if>
<div class="container">
    <div class="row mt-3">
        <div class="col-12">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <div class="row">
                        <div class="col-3 my-auto">
                            <h6 class="m-0 font-weight-bold text-primary">My Products</h6>
                        </div>
                        <div class="col-3 my-auto">
                            <h6 class="m-0 font-weight-bold text-primary" >Balance: RM <fmt:formatNumber value="${sellerBalance}" pattern="0.00"/></h6>
                        </div>
                        <div class="col-3 my-auto" >
                            <input type="text" class="form-control" id="filter" placeholder="Search"/>
                        </div>
                        <div class="col-3">
                            <a class="btn btn-outline-primary float-end" href="addNewProduct.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-circle-plus" width="28" height="28" viewBox="0 0 24 24" stroke-width="1.5" stroke="#00abfb" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                    <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                    <circle cx="12" cy="12" r="9" />
                                    <line x1="9" y1="12" x2="15" y2="12" />
                                    <line x1="12" y1="9" x2="12" y2="15" />
                                </svg>
                                Add New Products
                            </a>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <%
                            request.setAttribute("display","product");
                            request.setAttribute("productList", "seller");
                        %>
                        <jsp:include page="/DisplayData"/>
                        <c:if test="${not empty redirect}">
                            <%
                                request.getSession().removeAttribute("redirect");
                                response.sendRedirect(request.getContextPath() + "/home.jsp");
                            %>
                        </c:if>
                        <c:forEach var="product" items="${products}" varStatus="status">
                            <div class="col-3 productName">
                                <div class="card mb-2">
                                    <img src="<c:out value="data:image/jpeg;base64,${images[status.index]}" />" class="card-img-top img-fluid mx-auto my-auto" alt="Product Image">
                                    <div class="card-body">
                                        <h5 class="card-title" data-string="${product.productName}">${product.productName}</h5>
                                        <h6>RM <fmt:formatNumber value="${product.price}" pattern="0.00"/></h6>
                                        <div class="d-flex">
                                            <jsp:include page="/AverageRating?sellerId=${product.seller.id}"/>
                                            <h6>${avgRating}</h6>
                                            <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-star" width="20" height="20" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffbf00" fill="#ffbf00" stroke-linecap="round" stroke-linejoin="round">
                                                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                <path d="M12 17.75l-6.172 3.245l1.179 -6.873l-5 -4.867l6.9 -1l3.086 -6.253l3.086 6.253l6.9 1l-5 4.867l1.179 6.873z" />
                                            </svg>
                                        </div>
                                        <jsp:include page="/ProductTotalSales?productId=${product.id}"/>
                                        <p class="card-text"><small class="text-body-secondary">${totalSales} sold</small></p>
                                    </div>
                                    <div class="card-footer">
                                        <div class="d-flex float-end">
                                            <form action="ProductServlet" method="post">
                                                <input type="hidden" name="productId" value="${product.id}" readonly>
                                                <input type="hidden" name="action" value="getProduct" readonly>
                                                <button type="submit" class="btn btn-primary">
                                                    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-edit my-auto" width="18" height="18" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                                        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                        <path d="M9 7h-3a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-3" />
                                                        <path d="M9 15h3l8.5 -8.5a1.5 1.5 0 0 0 -3 -3l-8.5 8.5v3" />
                                                        <line x1="16" y1="5" x2="19" y2="8" />
                                                    </svg>
                                                </button>
                                            </form>
                                            <form action="ProductServlet" method="post">
                                                <input type="hidden" name="productId" value="${product.id}" readonly>
                                                <input type="hidden" name="action" value="deleteProduct" readonly>
                                                <button type="submit" class="btn btn-danger">
                                                    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-trash my-auto" width="18" height="18" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                                        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                        <line x1="4" y1="7" x2="20" y2="7" />
                                                        <line x1="10" y1="11" x2="10" y2="17" />
                                                        <line x1="14" y1="11" x2="14" y2="17" />
                                                        <path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" />
                                                        <path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" />
                                                    </svg>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
