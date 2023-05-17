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
    <title>Admin - Manage Admin</title>
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
                            <h6 class="m-0 font-weight-bold text-primary">Admin List</h6>
                        </div>
                        <div class="offset-4 col-4">
                            <button class="btn btn-outline-primary float-end" data-bs-toggle="modal" data-bs-target="#createAdminModal">
                                <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-circle-plus" width="28" height="28" viewBox="0 0 24 24" stroke-width="1.5" stroke="#00abfb" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                    <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                    <circle cx="12" cy="12" r="9" />
                                    <line x1="9" y1="12" x2="15" y2="12" />
                                    <line x1="12" y1="9" x2="12" y2="15" />
                                </svg>
                                Create Admin
                            </button>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable">
                            <thead>
                            <tr>
                                <th>Full Name</th>
                                <th>Staff ID</th>
                                <th>Email</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                request.setAttribute("display","admin");
                            %>
                                <jsp:include page="/DisplayData"/>
                                <c:forEach var="admin" items="${admins}">
                                    <tr id="row-${admin.id}">
                                        <td>${admin.fullName}</td>
                                        <td>${admin.staffId}</td>
                                        <td>${admin.getUser().email}</td>
                                    <td>
                                        <div class="row">
                                            <div class="col-2">
                                                <form action="AdminServlet" method="post">
                                                    <input type="hidden" name="adminId" value="${admin.id}" readonly>
                                                    <input type="hidden" name="action" value="getAdmin" readonly>
                                                    <button type="submit" class="btn btn-primary">
                                                        <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-edit my-auto" width="18" height="18" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff" fill="none" stroke-linecap="round" stroke-linejoin="round">
                                                            <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                                                            <path d="M9 7h-3a2 2 0 0 0 -2 2v9a2 2 0 0 0 2 2h9a2 2 0 0 0 2 -2v-3" />
                                                            <path d="M9 15h3l8.5 -8.5a1.5 1.5 0 0 0 -3 -3l-8.5 8.5v3" />
                                                            <line x1="16" y1="5" x2="19" y2="8" />
                                                        </svg>
                                                    </button>
                                                </form>
                                            </div>
                                            <div class="col-2">
                                                <form action="AdminServlet" method="post">
                                                    <input type="hidden" name="adminId" value="${admin.id}" readonly>
                                                    <input type="hidden" name="action" value="deleteAdmin" readonly>
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
<div class="modal fade" id="createAdminModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body">
                <div class="card shadow-2-strong card-registration rounded">
                    <div class="card-body p-4 p-md-5">
                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Add New Admin</h3>
                        <form class="row g-3 needs-validation" action="AdminServlet" method="post" novalidate >
                            <div class="col-md-6">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" name="email" class="form-control" id="email" required>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please insert your email.
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" name="password" class="form-control" id="password" required>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please insert your password.
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="validationCustom01" class="form-label">Full Name</label>
                                <input type="text" name="name" class="form-control" id="validationCustom01" required>
                                <div class="valid-feedback">
                                    Looks good!
                                </div>
                                <div class="invalid-feedback">
                                    Please insert your name.
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label for="staffId" class="form-label">Staff ID</label>
                                <div class="input-group has-validation">
                                    <span class="input-group-text" id="inputGroupPrepend">TP</span>
                                    <input type="text" name="staffId" class="form-control" id="staffId" pattern="[0-9]{6}" maxlength="6" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please enter a valid Staff ID (6 digit number).
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <button class="btn btn-primary" type="submit">Submit form</button>
                            </div>
                            <input type="hidden" name="action" value="createAdmin" readonly>
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
<c:if test="${not empty getAdmin}">
    <div class="modal fade" id="editAdminModal" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="card shadow-2-strong card-registration rounded">
                        <div class="card-body p-4 p-md-5">
                            <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Edit Admin Details</h3>
                            <form class="row g-3 needs-validation" action="AdminServlet" method="post" id="edit-form" novalidate >
                                <input type="hidden" name="adminId" value="${getAdmin.id}" readonly>
                                <div class="col-md-6">
                                    <label for="editEmail" class="form-label">Email</label>
                                    <input type="email" name="email" class="form-control" id="editEmail" value="${getAdmin.user.email}" required>
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
                                    <label for="editFullName" class="form-label">Full Name</label>
                                    <input type="text" name="name" class="form-control" id="editFullName" value="${getAdmin.fullName}" required>
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Please insert your name.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="editStaffId" class="form-label">Staff ID</label>
                                    <div class="input-group has-validation">
                                        <span class="input-group-text">TP</span>
                                        <input type="text" name="staffId" class="form-control" id="editStaffId" value="${getAdmin.staffId.substring(2)}" pattern="[0-9]{6}" maxlength="6" required>
                                        <div class="valid-feedback">
                                            Looks good!
                                        </div>
                                        <div class="invalid-feedback">
                                            Please enter a valid Staff ID (6 digit number).
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <button class="btn btn-primary" type="submit">Submit form</button>
                                </div>
                                <input type="hidden" name="action" value="editAdmin" readonly>
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
        const myModal = new bootstrap.Modal(document.getElementById('editAdminModal'), focus)
        const modalToggle = document.getElementById('editAdminModal')
        myModal.show(modalToggle);
    </script>
    <%
        request.getSession().removeAttribute("getAdmin");
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
