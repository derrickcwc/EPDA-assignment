package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CartFacade;
import com.example.shopoo.Facade.CustomerFacade;
import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.*;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {
    @EJB
    private CartFacade cartFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private ProductFacade productFacade;
    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        //Process Request based on action
        switch (action){
            case "addCart":
                addCart(request,response);
                break;
            case "deleteCart":
                deleteCart(request,response);
                break;
        }
    }

    private void deleteCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long cartId = Long.valueOf(request.getParameter("cartId"));
        Cart cart = cartFacade.getCartById(cartId);
        cartFacade.deleteCart(cart);

        // Update cartCount
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);
        if(customer != null){
            List<Cart> carts = cartFacade.getAllCartItems(customer);
            if(carts!= null){
                Integer cartCount = carts.size();
                request.getSession().setAttribute("cartCount",cartCount);
            }else {
                request.getSession().setAttribute("cartCount",0);
            }
        }

        String errorMsg = "Item removed from cart successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }

    private void addCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        Product product = productFacade.getProductById(productId);
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);

        if(customer != null && product != null){
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Cart cart = new Cart();
            cart.setCustomer(customer);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setCheckout(false);

            cartFacade.createCart(cart);

            // Update cartCount
            List<Cart> carts = cartFacade.getAllCartItems(customer);
            if(carts!= null){
                Integer cartCount = carts.size();
                request.getSession().setAttribute("cartCount",cartCount);
            }

            String errorMsg = "Item Added to cart successfully!";
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
 