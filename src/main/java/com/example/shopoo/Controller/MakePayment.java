package com.example.shopoo.Controller;

import com.example.shopoo.Facade.*;
import com.example.shopoo.Model.*;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MakePayment", value = "/MakePayment")
public class MakePayment extends HttpServlet {

    @EJB
    private PaymentFacade paymentFacade;
    @EJB
    private OrderFacade orderFacade;
    @EJB
    private CartFacade cartFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private CustomerFacade customerFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Add new payment
        Double totalAmount = Double.valueOf(request.getParameter("total"));
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);
        Double balance = customer.getBalance();
        if(balance == null){
            balance = 0.0;
        }
        if (balance >= totalAmount){
            // Update balance
            Double newBalance = customer.getBalance() - totalAmount;
            customer.setBalance(newBalance);
            customerFacade.editCustomer(customer);
            request.getSession().setAttribute("customerBalance", balance);

            // Add new Payment
            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setCollected(false);
            paymentFacade.createPayment(payment);

            // Get details
            Long sellerId = Long.valueOf(request.getParameter("sellerId"));
            Seller seller = sellerFacade.getSellerById(sellerId);

            // Add new Order
            Orders orders = new Orders();
            orders.setPayment(payment);
            orders.setSeller(seller);
            orders.setCustomer(customer);
            orderFacade.createOrder(orders);

            // Update Cart details (checkout)
            if(customer!= null){
                List<Cart> checkoutCarts = cartFacade.getUnpaidCartsBySeller(seller, customer);
                for(Cart cart : checkoutCarts){
                    cart.setOrder(orders);
                    cart.setCheckout(true);
                    cartFacade.editCart(cart);
                }
                // Update cartCount
                List<Cart> carts = cartFacade.getAllCartItems(customer);
                if(carts != null){
                    Integer cartCount = carts.size();
                    request.getSession().setAttribute("cartCount",cartCount);
                }else {
                    request.getSession().setAttribute("cartCount",0);
                }

            }

            request.getSession().setAttribute("customerBalance", newBalance);
            String errorMsg = "Thank you for your purchase!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        }else {
            String errorMsg = "Insufficient Balance!";
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
 