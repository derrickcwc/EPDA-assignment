package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CustomerFacade;
import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TopUpBalance", value = "/TopUpBalance")
public class TopUpBalance extends HttpServlet {

    @EJB
    CustomerFacade customerFacade;
    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);
        double topUpBalance = Double.parseDouble(request.getParameter("amount"));
        if(customer != null){
            Double balance = customer.getBalance();
            if (balance == null){
                balance = 0.0;
            }
            balance += topUpBalance;
            customer.setBalance(balance);
            customerFacade.editCustomer(customer);

            request.getSession().setAttribute("customerBalance", balance);

            String errorMsg = "Successfully Top-up";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/home.jsp");
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
 