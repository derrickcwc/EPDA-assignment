<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Product Details</title>
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/productDetails.css">
</head>
<body>
<!-- Navigation-->
<%@ include file="Utils/navbar.jsp" %>
<c:if test="${empty sessionScope.user}">
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
<c:if test="${empty getProduct}">
    <%
        response.sendRedirect(request.getContextPath() + "/home.jsp");
    %>
</c:if>
<c:if test="${not empty getProduct}">
<div class="container">
    <div class="row">
        <div class="col-10 offset-1">
            <div class="card shadow mb-4 mt-3">
                <div class="card-header py-3">
                    <div class="row">
                        <div class="col-4 my-auto">
                            <h6 class="m-0 font-weight-bold text-danger-emphasis">${getProduct.productName}</h6>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-5">
                                <%--                            <img src="assets/images/dino2.png" class="img-fluid mx-auto my-auto" alt="Product Image">--%>
                            <img src="<c:out value="data:image/jpeg;base64,${productImage}" />" class="img-fluid mx-auto my-auto" alt="Product Image">
                        </div>
                        <div class="col-7 mt-2">
                            <h4>${getProduct.productName}</h4>
                            <p>${getProduct.description}</p>
                            <div class="d-flex flex-row">
                                <jsp:include page="/AverageRating?sellerId=${getProduct.seller.id}"/>
                                <h6>${avgRating}</h6>
                                <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-star" width="20" height="20" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffbf00" fill="#ffbf00" stroke-linecap="round" stroke-linejoin="round">
                                    <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                    <path d="M12 17.75l-6.172 3.245l1.179 -6.873l-5 -4.867l6.9 -1l3.086 -6.253l3.086 6.253l6.9 1l-5 4.867l1.179 6.873z" />
                                </svg>
                                <jsp:include page="/ProductTotalSales?productId=${getProduct.id}"/>
                                <h6 class="px-2">${totalSales} Sold</h6>
                            </div>
                            <div class="row mt-3">
                                <div class="col-6">
                                    <h5>From: ${getProduct.seller.shopName}</h5>
                                </div>
                            </div>
                            <div class="row mt-5">
                                <div class="col-12">
                                    <h2 class="text-danger">RM <fmt:formatNumber value="${getProduct.price}" pattern="0.00"/></h2>
                                </div>
                            </div>
                            <form action="CartServlet" method="post">
                                <div class="row mt-3">
                                    <div class="col-4">
                                        <h3 class="text-secondary">Quantity :</h3>
                                    </div>
                                    <div class="col-4 my-auto">
                                        <input type="number" step="1" min="1" max="99" value="1" name="quantity" class="w-50">
                                    </div>
                                </div>
                                <div class="row mt-5">
                                    <div class="col-12">
                                        <input type="hidden" name="productId" value="${getProduct.id}">
                                        <input type="hidden" name="action" value="addCart">
                                        <button type="submit" class="btn btn-primary float-end">Add to Cart</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <%
        request.getSession().removeAttribute("getProduct");
        request.getSession().removeAttribute("productImage");
    %>
</c:if>
</body>
</html>
