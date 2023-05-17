package com.example.shopoo.Controller;

import com.example.shopoo.Facade.OrderFacade;
import com.example.shopoo.Facade.PaymentFacade;
import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Model.Orders;
import com.example.shopoo.Model.Payment;
import com.example.shopoo.Model.Product;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet(name = "GenerateReportSummary", value = "/GenerateReportSummary")
public class GenerateReportSummary extends HttpServlet {

    @EJB
    private OrderFacade orderFacade;
    @EJB
    private PaymentFacade paymentFacade;
    @EJB
    private ProductFacade productFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = productFacade.getAllProducts();
        List<Payment> payments = paymentFacade.getAllPayments();
        List<Orders> ordersList = orderFacade.getAllOrders();

        int totalProducts = productList.size();
        int totalOrders = ordersList.size();
        Double totalAmount = 0.0;
        for(Payment payment : payments){
            totalAmount+= payment.getAmount();
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String totalPayments = decimalFormat.format(totalAmount);

        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalPayments", totalPayments);

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
 