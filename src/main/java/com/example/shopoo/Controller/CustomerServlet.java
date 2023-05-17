package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CustomerFacade;
import com.example.shopoo.Facade.UserFacade;
import com.example.shopoo.Model.Admin;
import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.Seller;
import com.example.shopoo.Model.UserLog;
import com.example.shopoo.PasswordHashing;
import com.example.shopoo.StaticVar;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "CustomerServlet", value = "/CustomerServlet")
public class CustomerServlet extends HttpServlet {
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private UserFacade userFacade;

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String action = request.getParameter("action");

        // Process Request based on action
        switch (action) {
            case "register":
                registerCustomer(request, response);
                break;
            case "editCustomer":
                editCustomer(request,response);
                break;
            case "deleteCustomer":
                deleteCustomer(request,response);
                break;
            case "getCustomer":
                getCustomer(request,response);
                break;
            case "getProfile":
                getProfile(request,response);
        }
    }

    private void getCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long customerId = Long.valueOf(request.getParameter("customerId"));
        Customer customer = customerFacade.getCustomerById(customerId);

        request.getSession().setAttribute("getCustomer", customer);
        response.sendRedirect(request.getContextPath() + "/ManageCustomer.jsp");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long customerId = Long.valueOf(request.getParameter("customerId"));
        Customer customer = customerFacade.getCustomerById(customerId);
        UserLog user = customer.getUser();

        customerFacade.deleteCustomer(customer);

        // If customer has Multi role, do not delete user
        if(user.getUserType() == StaticVar.userType.Multi){
            user.setUserType(StaticVar.userType.Seller);
            userFacade.editUser(user);
        }else {
            userFacade.deleteUser(user);
        }

        String errorMsg = "Customer Deleted successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/ManageCustomer.jsp");
    }
    private void editCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        Long customerId = Long.valueOf(request.getParameter("customerId"));
        Customer customer = customerFacade.getCustomerById(customerId);
        UserLog user = customer.getUser();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String street = request.getParameter("street");
        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");

        // Check if email has been used
        if(userFacade.getUserByEmail(email) != null && !email.equals(user.getEmail())) {
            String errorMsg = "This email has been used, please try again using another email.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageCustomer.jsp");

        }else{
            // Process password changes (if any)
            if(password != null && !password.isEmpty()){
                // Hash the password using SHA-256
                PasswordHashing hasher = new PasswordHashing();
                String hashedPassword = hasher.hashPassword(password);
                user.setPassword(hashedPassword);
            }

            user.setEmail(email);
            userFacade.editUser(user);

            customer.setName(name);
            customer.setPhoneNumber(phone);
            customer.setStreet(street);
            customer.setState(state);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            customer.setUser(user);
            customerFacade.editCustomer(customer);

            UserLog sessionUser = (UserLog) request.getSession().getAttribute("user");
            StaticVar.userType userType= sessionUser.getUserType();
            String errorMsg = "Customer Updated successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);

            if(userType.equals(StaticVar.userType.Admin)){
                response.sendRedirect(request.getContextPath() + "/ManageCustomer.jsp");
            } else if (userType.equals(StaticVar.userType.Customer) || userType.equals(StaticVar.userType.Multi)){
                response.sendRedirect(request.getContextPath() + "/home.jsp");
            }

        }
    }
    private void registerCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        UserLog user = (UserLog) request.getSession().getAttribute("user");

        if(user != null){
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String street = request.getParameter("street");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String postalCode = request.getParameter("postalCode");

            // If user is already a Seller, change userType to Multi
            if(user.getUserType() == StaticVar.userType.Seller){
                user.setUserType(StaticVar.userType.Multi);
            }else {
                user.setUserType(StaticVar.userType.Customer);
            }

            Customer customer = new Customer();
            customer.setName(name);
            customer.setPhoneNumber(phone);
            customer.setStreet(street);
            customer.setState(state);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            customer.setUser(user);
            customer.setBalance(0.0);

            customerFacade.createCustomer(customer);
            userFacade.editUser(user);

            request.getSession().setAttribute("customerBalance", 0.0);
            String errorMsg = "Registered Successfully";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/home.jsp");

        }else {
            String errorMsg = "You are not logged in. Please log in to an account to continue.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    private void getProfile(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);

        request.setAttribute("getCustomer", customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/editCustomerProfile.jsp");
        dispatcher.forward(request,response);
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
 