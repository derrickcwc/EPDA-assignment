package com.example.shopoo.Controller;

import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.Seller;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

@WebServlet(name = "GetSellerBalance", value = "/GetSellerBalance")
public class GetSellerBalance extends HttpServlet {

    @EJB
    private SellerFacade sellerFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Seller seller = sellerFacade.getSeller(user);
        Double balance = seller.getBalance();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String bal = decimalFormat.format(balance);
        request.setAttribute("totalBalance", bal);
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
 