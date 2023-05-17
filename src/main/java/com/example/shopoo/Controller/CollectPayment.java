package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CartFacade;
import com.example.shopoo.Facade.OrderFacade;
import com.example.shopoo.Facade.PaymentFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.*;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet(name = "CollectPayment", value = "/CollectPayment")
public class CollectPayment extends HttpServlet {

    @EJB
    private PaymentFacade paymentFacade;
    @EJB
    private OrderFacade orderFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private CartFacade cartFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Seller seller = sellerFacade.getSeller(user);
        String order = request.getParameter("orderId");
        if(order!= null){
            Long orderId = Long.valueOf(order);
            Orders orders = orderFacade.getOrderById(orderId);
            Payment payment = paymentFacade.getPaymentById(orders.getPayment().getId());
            List<Cart> carts = cartFacade.getCartByOrderId(orderId);

            if(payment.getCollected()){
                String errorMsg = "The payment is already collected";
                request.getSession().setAttribute("errorMsg", errorMsg);
                response.sendRedirect(request.getContextPath() + "/sellerFeedback.jsp");
            }else {
                Double balance = seller.getBalance();
                if (balance == null){
                    balance = 0.0;
                }
                balance += payment.getAmount();
                seller.setBalance(balance);

                payment.setCollected(true);
                orders.setPayment(payment);

                sellerFacade.editSeller(seller);
                paymentFacade.editPayment(payment);
                orderFacade.editOrder(orders);

                for (Cart cart : carts){
                    cart.setOrder(orders);
                    cartFacade.editCart(cart);
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String bal = decimalFormat.format(balance);
                request.getSession().setAttribute("sellerBalance", balance);

                String errorMsg = "Payment collected. Your new balance is RM " + bal;
                request.getSession().setAttribute("errorMsg", errorMsg);
                response.sendRedirect(request.getContextPath() + "/sellerFeedback.jsp");
            }
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
 