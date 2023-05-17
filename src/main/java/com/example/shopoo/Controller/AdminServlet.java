package com.example.shopoo.Controller;

import com.example.shopoo.Facade.AdminFacade;
import com.example.shopoo.Facade.UserFacade;
import com.example.shopoo.Model.Admin;
import com.example.shopoo.Model.UserLog;
import com.example.shopoo.PasswordHashing;
import com.example.shopoo.StaticVar;
import com.example.shopoo.Controller.UserServlet;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @EJB
    private UserFacade userFacade;
    @EJB
    private AdminFacade adminFacade;
    public void init() {

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getParameter("action") != null){
            String action = (String) request.getParameter("action");
            //Process Request based on action
            switch (action){
                case "createAdmin":
                    createAdmin(request,response);
                    break;
                case "deleteAdmin":
                    deleteAdmin(request,response);
                    break;
                case "editAdmin":
                    editAdmin(request,response);
                    break;
                case "getAdmin":
                    getAdmin(request,response);
                    break;
            }
        }
    }

    private void getAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long adminId = Long.valueOf(request.getParameter("adminId"));
        Admin admin = adminFacade.getAdminById(adminId);

        request.getSession().setAttribute("getAdmin", admin);
        response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");
    }

    private void editAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long adminId = Long.valueOf(request.getParameter("adminId"));
        Admin admin = adminFacade.getAdminById(adminId);
        UserLog user = admin.getUser();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String staffId = "TP" + request.getParameter("staffId");

        // Check if email or Staff ID has been used
        if(userFacade.getUserByEmail(email) != null && !email.equals(user.getEmail())) {
            String errorMsg = "This email has been used, please try again using another email.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");

        } else if(adminFacade.getAdminByStaffId(staffId) != null && !staffId.equals(admin.getStaffId())){
            String errorMsg = "This Staff ID already exist, please try again using another Staff ID.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");

        }else {
            // Process password changes (if any)
            if(password != null && !password.isEmpty()){
                // Hash the password using SHA-256
                PasswordHashing hasher = new PasswordHashing();
                String hashedPassword = hasher.hashPassword(password);
                user.setPassword(hashedPassword);
            }

            user.setEmail(email);
            userFacade.editUser(user);

            admin.setFullName(name);
            admin.setStaffId(staffId);
            admin.setUser(user);
            adminFacade.editAdmin(admin);

            String errorMsg = "Admin Updated successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");
        }
    }
    
    private void createAdmin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String staffId = "TP" + request.getParameter("staffId");

        // Hash the password using SHA-256
        PasswordHashing hasher = new PasswordHashing();
        String hashedPassword = hasher.hashPassword(password);

        // Check if email or Staff ID has been used
        if(userFacade.getUserByEmail(email) != null) {
            String errorMsg = "This email has been used, please try again using another email.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");

        } else if(adminFacade.getAdminByStaffId(staffId) != null){
            String errorMsg = "This Staff ID already exist, please try again using another Staff ID.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");

        } else {
            UserLog user = new UserLog();
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setUserType(StaticVar.userType.Admin);
            userFacade.create(user);

            Admin admin = new Admin();
            admin.setFullName(name);
            admin.setStaffId(staffId);
            admin.setUser(user);
            adminFacade.createAdmin(admin);

            String errorMsg = "New Admin added successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");
        }
    }
    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long adminId = Long.valueOf(request.getParameter("adminId"));
        Admin admin = adminFacade.getAdminById(adminId);
        UserLog user = admin.getUser();
        adminFacade.deleteAdmin(admin);
        userFacade.deleteUser(user);

        String errorMsg = "Admin Deleted successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/ManageAdmin.jsp");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void destroy() {
    }
}
 