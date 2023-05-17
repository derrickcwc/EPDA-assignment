<%@ page import="com.example.shopoo.StaticVar" %><%--
  Created by IntelliJ IDEA.
  User: derri
  Date: 5/5/2023
  Time: 3:40 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin - Manage Seller</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <link href="datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
    <script src="js/ManageAdmin.js"></script>
</head>
<body>
<%@ include file="Utils/adminNav.jsp" %>
<c:if test="${empty user}">
    <%
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    %>
</c:if>
<c:if test="${user.userType ne 'Admin'}">
    <%
        response.sendRedirect(request.getContextPath() + "/home.jsp");
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

<div class="container">
    <div class="row mt-3">
        <div class="col-12">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <div class="row">
                        <div class="col-4 my-auto">
                            <h6 class="m-0 font-weight-bold text-primary">Seller List</h6>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable">
                            <thead>
                            <tr>
                                <th>Shop Name</th>
                                <th>Shop Rating</th>
                                <th>Phone Number</th>
                                <th>Street</th>
                                <th>State</th>
                                <th>City</th>
                                <th>Postal Code</th>
                                <th>Status</th>
                                <th>Email</th>
                                <th>Description</th>
                                <th>Action</th>

                            </tr>
                            </thead>
                            <tbody>
                            <%
                                request.setAttribute("display","seller");
                            %>
                            <jsp:include page="/DisplayData"/>
                            <c:forEach var="seller" items="${sellers}">
                                <tr id="row-${seller.id}">
                                    <td>${seller.shopName}</td>
                                    <jsp:include page="/AverageRating?sellerId=${seller.id}"/>
                                    <td>${avgRating}</td>
                                    <td>${seller.phoneNumber}</td>
                                    <td>${seller.street}</td>
                                    <td>${seller.state}</td>
                                    <td>${seller.city}</td>
                                    <td>${seller.postalCode}</td>
                                    <td>${seller.status}</td>
                                    <td>${seller.getUser().email}</td>
                                    <td>${seller.description}</td>
                                    <td>
                                        <div class="row">
                                            <c:if test="${seller.status == 'Pending'}">
                                                <div class="col-4">
                                                    <form action="SellerServlet" method="post">
                                                        <input type="hidden" name="sellerId" value="${seller.id}" readonly>
                                                        <input type="hidden" name="action" value="approveSeller" readonly>
                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-checkbox" width="18" height="18" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                                                <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                                <polyline points="9 11 12 14 20 6" />
                                                                <path d="M20 12v6a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2h9" />
                                                            </svg>
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:if>
                                            <div class="col-4">
                                                <form action="SellerServlet" method="post">
                                                    <input type="hidden" name="sellerId" value="${seller.id}" readonly>
                                                    <input type="hidden" name="action" value="getSeller" readonly>
                                                    <button type="submit" class="btn btn-primary btn-sm">
                                                        <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-edit my-auto" width="18" height="18" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                                            <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                            <path d="M9 7h-3a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-3" />
                                                            <path d="M9 15h3l8.5 -8.5a1.5 1.5 0 0 0 -3 -3l-8.5 8.5v3" />
                                                            <line x1="16" y1="5" x2="19" y2="8" />
                                                        </svg>
                                                    </button>
                                                </form>
                                            </div>
                                            <div class="col-4">
                                                <form action="SellerServlet" method="post">
                                                    <input type="hidden" name="sellerId" value="${seller.id}" readonly>
                                                    <input type="hidden" name="action" value="deleteSeller" readonly>
                                                    <button type="submit" class="btn btn-danger btn-sm">
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
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<c:if test="${not empty getSeller}">
    <div class="modal fade" id="editSellerModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="card shadow-2-strong card-registration rounded">
                        <div class="card-body p-4 p-md-5">
                            <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Edit Seller Details</h3>
                            <form class="row g-3 needs-validation" action="SellerServlet" method="post" novalidate >
                                <input type="hidden" name="sellerId" value="${getSeller.id}" readonly>
                                <div class="col-md-6">
                                    <label for="editEmail" class="form-label">Email</label>
                                    <input type="email" name="email" class="form-control" id="editEmail" value="${getSeller.user.email}" required>
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
                                    <input type="text" name="shopName" class="form-control" id="validationCustom01" value="${getSeller.shopName}" required>
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
                                        <input type="tel" name="phone" class="form-control" id="validationCustomPhone" aria-describedby="inputGroupPrepend" pattern="\d{8,10}" minlength="8" maxlength="10" value="${getSeller.phoneNumber}" required>
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
                                    <input type="text" name="street" class="form-control" id="street" value="${getSeller.street}" required>
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
                                        <option selected value="${getSeller.state}">${getSeller.state}</option>
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
                                    <input type="text" name="city" class="form-control" id="city" value="${getSeller.city}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please provide a valid city.
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <label for="postalCode" class="form-label">Postal Code</label>
                                    <input type="text" name="postalCode" class="form-control" id="postalCode" pattern="[0-9]{5}" maxlength="5" value="${getSeller.postalCode}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please provide a valid postal code.
                                    </div>
                                </div>
                                <div class="col-12">
                                    <label for="floatingTextarea" class="form-label">Shop Description</label>
                                    <div class="form-floating">
                                        <textarea class="form-control" name="description" placeholder="Describe your shop" id="floatingTextarea" maxlength="150" style="height: 100px">${getSeller.description}</textarea>
                                        <label for="floatingTextarea">Describe your shop here... (150 characters)</label>
                                    </div>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please describe your shop.
                                    </div>
                                </div>
                                <div class="col-12">
                                    <button class="btn btn-primary" type="submit">Submit form</button>
                                </div>
                                <input type="hidden" name="action" value="editSeller" readonly>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        const myModal = new bootstrap.Modal(document.getElementById('editSellerModal'), focus)
        const modalToggle = document.getElementById('editSellerModal')
        myModal.show(modalToggle);
    </script>
    <%
        request.getSession().removeAttribute("getSeller");
    %>
</c:if>
</body>
<script src="datatables/jquery.dataTables.min.js"></script>
<script src="datatables/dataTables.bootstrap4.min.js"></script>
<script>
    $(document).ready(function() {
        $('#dataTable').DataTable();
    });
</script>
</html>
