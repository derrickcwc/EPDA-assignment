<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Purchase History</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link rel="stylesheet" href="css/customerFeedback.css">
    <script src="js/customerFeedback.js"></script>
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
<body>
<div class="container">
    <div class="row my-3">
        <div class="col-10 offset-1">
            <%
                request.setAttribute("display","purchaseHistory");
            %>
            <jsp:include page="/DisplayData"/>
            <c:if test="${empty carts}">
                <%
                    response.sendRedirect(request.getContextPath() + "/home.jsp");
                %>
            </c:if>
            <c:forEach var="cartSeller" items="${carts}" varStatus="orderStatus">
                <div class="card mb-3">
                    <div class="card-header py-3">
                        <div class="row">
                            <div class="col-4 my-auto">
                                <h6 class="m-0 font-weight-bold text-danger-emphasis">${cartSeller[0].order.orderDate}</h6>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-12">
                                <c:forEach var="cartItem" items="${cartSeller}" varStatus="itemStatus">
                                    <div class="card">
                                        <div class="row">
                                            <div class="col-4">
                                                <div class="mx-2">
                                                    <h5>${cartItem.product.productName}</h5>
                                                    <div class="row mt-3">
                                                        <div class="col-6">
                                                            <h6>From: ${cartItem.product.seller.shopName}</h6>
                                                        </div>
                                                    </div>
                                                    <div class="row mt-5">
                                                        <div class="col-12">
                                                            <h3 class="text-danger">RM <fmt:formatNumber value="${cartItem.product.price}" pattern="0.00"/></h3>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-3 d-flex align-items-center">
                                                <h5 class="text-secondary float-end">Quantity : ${cartItem.quantity}</h5>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <div class="float-end d-flex">
                            <h4>Order Total: &nbsp;</h4>
                            <h4 class="text-danger"> RM ${orderTotal[orderStatus.index]}</h4>
                        </div>
                    </div>
                    <div class="card-footer">
                        <%
                            request.setAttribute("display","feedback");
                        %>
                        <jsp:include page="/DisplayData?orderId=${cartSeller[0].order.id}"/>
                        <c:choose>
                            <c:when test="${feedback.orders.id == cartSeller[0].order.id }">
                                <div class='feedback-rating-stars'>
                                    <ul class='stars'>
                                        <li class='star' title='Poor' data-value='1'>
                                            <i class='fa fa-star fa-fw'></i>
                                        </li>
                                        <li class='star' title='Fair' data-value='2'>
                                            <i class='fa fa-star fa-fw'></i>
                                        </li>
                                        <li class='star' title='Good' data-value='3'>
                                            <i class='fa fa-star fa-fw'></i>
                                        </li>
                                        <li class='star' title='Excellent' data-value='4'>
                                            <i class='fa fa-star fa-fw'></i>
                                        </li>
                                        <li class='star' title='Perfect' data-value='5'>
                                            <i class='fa fa-star fa-fw'></i>
                                        </li>
                                    </ul>
                                    <input type="hidden" id="feedback-rating" value="${feedback.rating}">
                                    <p>${feedback.comment}</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <form action="SubmitFeedback?orderId=${cartSeller[0].order.id}&action=customer" method="post">
                                    <label for="starrate" class="form-label">Rate your experience</label>
                                    <section class='rating-widget' id="starrate">
                                        <!-- Rating Stars Box -->
                                        <div class='rating-stars'>
                                            <ul class='stars'>
                                                <li class='star' title='Poor' data-value='1'>
                                                    <i class='fa fa-star fa-fw'></i>
                                                </li>
                                                <li class='star' title='Fair' data-value='2'>
                                                    <i class='fa fa-star fa-fw'></i>
                                                </li>
                                                <li class='star' title='Good' data-value='3'>
                                                    <i class='fa fa-star fa-fw'></i>
                                                </li>
                                                <li class='star' title='Excellent' data-value='4'>
                                                    <i class='fa fa-star fa-fw'></i>
                                                </li>
                                                <li class='star' title='Perfect' data-value='5'>
                                                    <i class='fa fa-star fa-fw'></i>
                                                </li>
                                            </ul>
                                            <input type="hidden" name="rating" class="rating" value="" required>
                                        </div>
                                    </section>
                                    <div class="form-floating">
                                        <textarea class="form-control" name="description" placeholder="Describe your shop" id="floatingTextarea" maxlength="150" style="height: 100px" required>${getSeller.description}</textarea>
                                        <label for="floatingTextarea">Tell us about your experience</label>
                                    </div>
                                    <button type="submit" class="btn btn-primary float-end mt-2" >Submit Feedback</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
