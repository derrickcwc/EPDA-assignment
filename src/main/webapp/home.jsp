<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Shop Homepage - Start Bootstrap Template</title>
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/home.css">
</head>
<body>
<!-- Navigation-->
<%@ include file="Utils/navbar.jsp" %>
<c:if test="${empty sessionScope.user}">
    <%
        response.sendRedirect(request.getContextPath() + "/login.jsp");
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

<!-- Header-->
<header class="bg-dark py-5">
    <div class="container px-4 px-lg-5 my-5">
        <div class="text-center text-white">
            <h1 class="display-4 fw-bolder">Shopoo</h1>
            <p class="lead fw-normal text-white-50 mb-0">Discover the Best Deals on Fashion, Electronics, and More!</p>
        </div>
    </div>
</header>
<!-- Section-->
<section class="py-5">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <div class="row">
                            <div class="col-4 my-auto">
                                <h6 class="m-0 font-weight-bold text-danger-emphasis">Products On Sale</h6>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <%
                                request.setAttribute("display","product");
                                request.setAttribute("productList", "customer");
                            %>
                            <jsp:include page="/DisplayData"/>
                            <c:forEach var="product" items="${products}" varStatus="status">
                                <div class="col-3">
                                    <div class="card mb-2">
                                        <img src="<c:out value="data:image/jpeg;base64,${images[status.index]}" />" class="card-img-top img-fluid mx-auto my-auto" alt="Product Image">
                                        <div class="card-body">
                                            <h5 class="card-title">${product.productName}</h5>
                                            <h6 class="text-danger">RM <fmt:formatNumber value="${product.price}" pattern="0.00"/></h6>
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
                                                <input type="hidden" name="productId" value="${product.id}" readonly>
                                                <a href="ProductServlet?productId=${product.id}&action=viewProduct">
                                                    <button type="button" class="btn btn-primary">View Product</button>
                                                </a>
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
    </div>
</section>
</body>
</html>
