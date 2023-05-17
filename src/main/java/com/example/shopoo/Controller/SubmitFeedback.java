package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CustomerFacade;
import com.example.shopoo.Facade.FeedbackFacade;
import com.example.shopoo.Facade.OrderFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.*;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SubmitFeedback", value = "/SubmitFeedback")
public class SubmitFeedback extends HttpServlet {

    @EJB
    private FeedbackFacade feedbackFacade;
    @EJB
    private OrderFacade orderFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private SellerFacade sellerFacade;

    public void init() {

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long orderId = Long.valueOf(request.getParameter("orderId"));
        Orders orders = orderFacade.getOrderById(orderId);
        String rateString = request.getParameter("rating");
        Double rating = Double.valueOf(rateString);
        String description = request.getParameter("description");

        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Feedback feedback = new Feedback();

        if(request.getParameter("action").equals("customer")){
            Customer customer = customerFacade.getCustomer(user);
            feedback.setCustomer(customer);
        }else if(request.getParameter("action").equals("seller")){
            Seller seller = sellerFacade.getSeller(user);
            feedback.setSeller(seller);
        }

        feedback.setComment(description);
        feedback.setRating(rating);
        feedback.setOrders(orders);

        feedbackFacade.createFeedback(feedback);

        String errorMsg = "Thank you for your feedback!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/home.jsp");


    }

    private void customerFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
 