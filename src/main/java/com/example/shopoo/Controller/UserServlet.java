package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CartFacade;
import com.example.shopoo.Facade.CustomerFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Facade.UserFacade;
import com.example.shopoo.Model.*;
import com.example.shopoo.PasswordHashing;
import com.example.shopoo.StaticVar;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "userServlet", value = "/user-servlet")
public class UserServlet extends HttpServlet {

    // Inject the UserFacade singleton
    @EJB
    private UserFacade userFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private CartFacade cartFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Process Request based on action
        switch (action){
            case "login":
                loginUser(request,response);
                break;
            case "register":
                registerUser(request,response);
                break;
            case "delete":
                deleteUser(request,response);
                break;
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("userId"));
        UserLog user = userFacade.getUserById(userId);
        userFacade.deleteUser(user);

        String errorMsg = "User Deleted successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/ManageUser.jsp");
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hash the password using SHA-256
        PasswordHashing hasher = new PasswordHashing();
        String hashedPassword = hasher.hashPassword(password);

        UserLog user = userFacade.login(email, hashedPassword);
        if(user != null) {
            // Login success, set user in session and redirect to home page
            request.getSession().setAttribute("user", user);

            // Redirect User based on user role
            if(user.getUserType() == StaticVar.userType.Admin){
                response.sendRedirect(request.getContextPath() + "/adminHome.jsp");
            }else {
                Customer customer = customerFacade.getCustomer(user);
                Seller seller = sellerFacade.getSeller(user);
                if(customer != null){
                    List<Cart> carts = cartFacade.getAllCartItems(customer);
                    if(carts!= null){
                        Integer cartCount = carts.size();
                        request.getSession().setAttribute("cartCount",cartCount);
                    }
                    Double customerBalance = customer.getBalance();
                    if (customerBalance == null){
                        customerBalance = 0.0;
                    }
                    request.getSession().setAttribute("customerBalance", customerBalance);
                }
                if(seller != null){
                    Double sellerBalance = seller.getBalance();
                    if (sellerBalance == null){
                        sellerBalance = 0.0;
                    }
                    request.getSession().setAttribute("sellerBalance", sellerBalance);
                }
                if(user.getUserType() == StaticVar.userType.Seller){
                    response.sendRedirect(request.getContextPath() + "/sellerPage.jsp");
                }else {
                    response.sendRedirect(request.getContextPath() + "/home.jsp");
                }
            }

        } else {
            // If login fails, show error message in modal
            String errorMsg = "Incorrect email or password.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hash the password using SHA-256
        PasswordHashing hasher = new PasswordHashing();
        String hashedPassword = hasher.hashPassword(password);

        // If email has not been used, proceed to register
        if(userFacade.getUserByEmail(email) == null){
            UserLog user = new UserLog();
            user.setEmail(email);
            user.setPassword(hashedPassword);

            userFacade.create(user);

            String errorMsg = "User Registered Successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/login.jsp");

        } else {
            String errorMsg = "This email has been used, please try again using another email.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/signup.jsp");
        }
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
}
