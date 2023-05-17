package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CartFacade;
import com.example.shopoo.Facade.OrderFacade;
import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Model.Cart;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ProductTotalSales", value = "/ProductTotalSales")
public class ProductTotalSales extends HttpServlet {

    @EJB
    private ProductFacade productFacade;
    @EJB
    private CartFacade cartFacade;

    public void init() {

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        List<Cart> carts = cartFacade.getSoldCartItemsByProductId(productId);

        int totalSales = 0;
        if(carts!= null){
            for(Cart cart : carts){
                int quantity = cart.getQuantity();
                totalSales += quantity;
            }
        }
        request.setAttribute("totalSales", totalSales);
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
 