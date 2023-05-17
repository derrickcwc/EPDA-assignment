package com.example.shopoo.Controller;

import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Facade.UserFacade;
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


@WebServlet(name = "SellerServlet", value = "/SellerServlet")
public class SellerServlet extends HttpServlet {
    @EJB
    private UserFacade userFacade;
    @EJB
    private SellerFacade sellerFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // Process Request based on action
        switch (action) {
            case "registerSeller":
                registerSeller(request, response);
                break;
            case "editSeller":
                editSeller(request,response);
                break;
            case "deleteSeller":
                deleteSeller(request,response);
                break;
            case "getSeller":
                getSeller(request,response);
                break;
            case "approveSeller":
                approveSeller(request,response);
                break;
            case "getProfile":
                getProfile(request,response);
                break;
        }
    }

    private void approveSeller(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long sellerId = Long.valueOf(request.getParameter("sellerId"));
        Seller seller = sellerFacade.getSellerById(sellerId);
        seller.setStatus(StaticVar.sellerStatus.Approved);
        sellerFacade.editSeller(seller);

        String errorMsg = "Seller Approved successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/ManageSeller.jsp");
    }

    private void deleteSeller(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long sellerId = Long.valueOf(request.getParameter("sellerId"));
        Seller seller = sellerFacade.getSellerById(sellerId);
        UserLog user = seller.getUser();

        sellerFacade.deleteSeller(seller);

        // If seller has Multi role, do not delete user
        if(user.getUserType() == StaticVar.userType.Multi){
            user.setUserType(StaticVar.userType.Customer);
            userFacade.editUser(user);
        }else {
            userFacade.deleteUser(user);
        }

        String errorMsg = "Seller Deleted successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/ManageSeller.jsp");
    }

    private void editSeller(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long sellerId = Long.valueOf(request.getParameter("sellerId"));
        Seller seller = sellerFacade.getSellerById(sellerId);
        UserLog user = seller.getUser();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String shopName = request.getParameter("shopName");
        String phone = request.getParameter("phone");
        String street = request.getParameter("street");
        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");
        String description = request.getParameter("description");

        // Check if email has been used
        if(userFacade.getUserByEmail(email) != null && !email.equals(user.getEmail())) {
            String errorMsg = "This email has been used, please try again using another email.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/ManageSeller.jsp");

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

            seller.setShopName(shopName);
            seller.setPhoneNumber(phone);
            seller.setStreet(street);
            seller.setState(state);
            seller.setCity(city);
            seller.setPostalCode(postalCode);
            seller.setUser(user);
            seller.setDescription(description);

            sellerFacade.editSeller(seller);

            UserLog sessionUser = (UserLog) request.getSession().getAttribute("user");
            StaticVar.userType userType= sessionUser.getUserType();
            String errorMsg = "Seller Updated successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);

            if(userType.equals(StaticVar.userType.Admin)){
                response.sendRedirect(request.getContextPath() + "/ManageSeller.jsp");
            } else if (userType.equals(StaticVar.userType.Seller) || userType.equals(StaticVar.userType.Multi)){
                response.sendRedirect(request.getContextPath() + "/sellerPage.jsp");
            }

        }
    }

    private void registerSeller(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");

        if(user != null){
            String shopName = request.getParameter("shopName");
            String phone = request.getParameter("phone");
            String street = request.getParameter("street");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String postalCode = request.getParameter("postalCode");
            String description = request.getParameter("description");

            // If user is already a Customer, change userType to Multi
            if(user.getUserType() == StaticVar.userType.Customer){
                user.setUserType(StaticVar.userType.Multi);
            }else {
                user.setUserType(StaticVar.userType.Seller);
            }

            Seller seller = new Seller();
            seller.setShopName(shopName);
            seller.setPhoneNumber(phone);
            seller.setStreet(street);
            seller.setState(state);
            seller.setCity(city);
            seller.setPostalCode(postalCode);
            seller.setUser(user);
            seller.setDescription(description);
            seller.setStatus(StaticVar.sellerStatus.Pending);

            sellerFacade.createSeller(seller);
            userFacade.editUser(user);

            String errorMsg = "Your shop details has been sent for review, kindly wait for approval.";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        }
    }

    private void getSeller(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long sellerId = Long.valueOf(request.getParameter("sellerId"));
        Seller seller = sellerFacade.getSellerById(sellerId);

        UserLog sessionUser = (UserLog) request.getSession().getAttribute("user");
        StaticVar.userType userType= sessionUser.getUserType();

        if(userType.equals(StaticVar.userType.Admin)) {
            request.getSession().setAttribute("getSeller", seller);
            response.sendRedirect(request.getContextPath() + "/ManageSeller.jsp");
        }
    }

    private void getProfile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Seller seller = sellerFacade.getSeller(user);

        request.setAttribute("getSeller", seller);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/editSellerProfile.jsp");
        dispatcher.forward(request,response);
//        request.getSession().setAttribute("getSeller", seller);
//        response.sendRedirect(request.getContextPath() + "/editSellerProfile.jsp");
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
 