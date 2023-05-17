package com.example.shopoo.Controller;

import com.example.shopoo.Facade.FeedbackFacade;
import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.Seller;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

@WebServlet(name = "AverageRating", value = "/AverageRating")
public class AverageRating extends HttpServlet {

    @EJB
    private FeedbackFacade feedbackFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private ProductFacade productFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long sellerId = Long.valueOf(request.getParameter("sellerId"));
        Double rating = feedbackFacade.getAverageRatingBySeller(sellerId);
        if (rating != null){
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String avgRating = decimalFormat.format(rating);
            request.setAttribute("avgRating", avgRating);
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


    public void destroy() {
    }
}
 