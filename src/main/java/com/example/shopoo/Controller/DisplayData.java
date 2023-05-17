package com.example.shopoo.Controller;

import com.example.shopoo.Facade.*;
import com.example.shopoo.Model.*;
import com.example.shopoo.StaticVar;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "DisplayData", value = "/DisplayData")
public class DisplayData extends HttpServlet {
    @EJB
    private UserFacade userFacade;
    @EJB
    private AdminFacade adminFacade;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private CartFacade cartFacade;
    @EJB
    private FeedbackFacade feedbackFacade;
    @EJB
    private OrderFacade orderFacade;
    @EJB
    private PaymentFacade paymentFacade;

    public void init() {

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getAttribute("display") != null){
            String display = (String) request.getAttribute("display");
            //Process Request based on action
            switch (display){
                case "user":
                    getUsers(request,response);
                    break;
                case "admin":
                    getAdmins(request,response);
                    break;
                case "customer":
                    getCustomers(request,response);
                    break;
                case "seller":
                    getSellers(request,response);
                    break;
                case "product":
                    getProducts(request,response);
                    break;
                case "cart":
                    getCartItems(request,response);
                    break;
                case "purchaseHistory":
                    getPurchaseHistory(request,response);
                    break;
                case "feedback":
                    getFeedback(request, response);
                    break;
                case "sales":
                    getSales(request,response);
                    break;
                case "sellerFeedback":
                    getSellerFeedback(request,response);
                    break;
                case "payment":
                    getPayment(request,response);
                    break;
                case "analysis":
                    getAnalysis(request, response);
                    break;
            }
        }
    }

    private void getAnalysis(HttpServletRequest request, HttpServletResponse response) {
        List<Feedback> feedbacks = feedbackFacade.getAllFeedbackByRating();
        if(feedbacks != null){
            request.setAttribute("feedbacks", feedbacks);

            int oneStar = 0;
            int twoStar = 0;
            int threeStar = 0;
            int fourStar = 0;
            int fiveStar = 0;

            for(Feedback feedback : feedbacks){
                Double rating = feedback.getRating();
                if (rating == 1.0) {
                    oneStar++;
                }else if (rating == 2.0){
                    twoStar++;
                }else if (rating == 3.0){
                    threeStar++;
                }else if (rating == 4.0){
                    fourStar++;
                }else if (rating == 5.0){
                    fiveStar++;
                }
            }
            request.setAttribute("oneStar", oneStar);
            request.setAttribute("twoStar", twoStar);
            request.setAttribute("threeStar", threeStar);
            request.setAttribute("fourStar", fourStar);
            request.setAttribute("fiveStar", fiveStar);
        }
    }

    private void getPayment(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.valueOf(request.getParameter("orderId"));
        Orders orders = orderFacade.getOrderById(orderId);
        Payment payment = paymentFacade.getPaymentById(orders.getPayment().getId());

        Boolean paymentStatus = payment.getCollected();
        request.setAttribute("paymentStatus",paymentStatus);
    }

    private void getSellerFeedback(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.valueOf(request.getParameter("orderId"));
        Orders orders = orderFacade.getOrderById(orderId);
        Feedback sellerFeedback = feedbackFacade.getSellerFeedbackByOrder(orders);
        if(sellerFeedback != null){
            request.setAttribute("sellerFeedback",sellerFeedback);
        }
    }


    private void getFeedback(HttpServletRequest request, HttpServletResponse response) {
        Long orderId = Long.valueOf(request.getParameter("orderId"));
        Orders orders = orderFacade.getOrderById(orderId);
        Feedback feedback = feedbackFacade.getCustomerFeedbackByOrder(orders);
        if(feedback != null){
            request.setAttribute("feedback",feedback);
        }
    }
    private void getSales(HttpServletRequest request, HttpServletResponse response) {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Seller seller = sellerFacade.getSeller(user);
        if(seller != null){
            List<Cart> cartItems = cartFacade.getSalesBySeller(seller);
            List<Orders> ordersList = orderFacade.getAllOrdersBySeller(seller);

            if(cartItems != null && ordersList != null){
                // Sort the cart items based on the orders
                List<List<Cart>> sortedList = new ArrayList<>();

                // Calculate Order Total
                List<String> orderTotal = new ArrayList<>();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                for(Orders orders : ordersList){
                    List<Cart> carts = new ArrayList<>();
                    for(Cart cartItem : cartItems){
                        Long ordersId = orders.getId();
                        Long cartItemOrdersId = cartItem.getOrder().getId();
                        if(ordersId.equals(cartItemOrdersId)){
                            carts.add(cartItem);
                        }
                    }
                    sortedList.add(carts);

                    Double total = orders.getPayment().getAmount();
                    String formattedTotal = decimalFormat.format(total);
                    orderTotal.add(formattedTotal);
                }
                request.setAttribute("carts", sortedList);
                request.setAttribute("orderTotal", orderTotal);
            }
        }
    }

    private void getPurchaseHistory(HttpServletRequest request, HttpServletResponse response) {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);

        if(customer!=null){
            List<Cart> cartItems = cartFacade.getPurchaseHistoryByCustomer(customer);
            List<Orders> ordersList = orderFacade.getAllOrdersByCustomer(customer);

            if(cartItems != null && ordersList != null){
                // Sort the cart items based on the orders
                List<List<Cart>> sortedList = new ArrayList<>();

                // Calculate Order Total
                List<String> orderTotal = new ArrayList<>();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                for(Orders orders : ordersList){
                    List<Cart> carts = new ArrayList<>();
                    for(Cart cartItem : cartItems){
                        Long ordersId = orders.getId();
                        Long cartItemOrdersId = cartItem.getOrder().getId();
                        if(ordersId.equals(cartItemOrdersId)){
                            carts.add(cartItem);
                        }
                    }
                    sortedList.add(carts);

                    Double total = orders.getPayment().getAmount();
                    String formattedTotal = decimalFormat.format(total);
                    orderTotal.add(formattedTotal);
                }
                request.setAttribute("carts", sortedList);
                request.setAttribute("orderTotal", orderTotal);
            }
        }
    }

    private void getCartItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Customer customer = customerFacade.getCustomer(user);

        if(customer!= null){
            List<Cart> cartItems = cartFacade.getAllCartItems(customer);
            List<Seller> cartSellers = cartFacade.getAllCartSellers(customer);
            if(cartItems != null){
                // Sort the cart items based on the sellers
                List<List<Cart>> sortedList = new ArrayList<>();

                // Calculate Order Total
                List<String> orderTotal = new ArrayList<>();
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                for(Seller seller : cartSellers){
                    List<Cart> carts = new ArrayList<>();
                    Double total = 0.0;
                    for(Cart cartItem : cartItems){
                        Long sellerId = seller.getId();
                        Long cartItemSellerId = cartItem.getProduct().getSeller().getId();
                        if(sellerId.equals(cartItemSellerId)){
                            carts.add(cartItem);
                            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();

                        }
                    }
                    sortedList.add(carts);
                    String formattedTotal = decimalFormat.format(total);
                    orderTotal.add(formattedTotal);
                }
                request.setAttribute("carts", sortedList);
                request.setAttribute("orderTotal", orderTotal);
            } else {
                String errorMsg = "Cart is empty, start shopping now!";
                request.getSession().setAttribute("errorMsg", errorMsg);
                response.sendRedirect(request.getContextPath() + "/home.jsp");
            }

        }else {
            String errorMsg = "Please register as customer first!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/newCustomer.jsp");
        }
    }

    private void getProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = new ArrayList<>();

        if(request.getAttribute("productList").equals("seller")){

            UserLog user = (UserLog) request.getSession().getAttribute("user");
            Seller seller = sellerFacade.getSeller(user);

            if(seller!= null){
                if (seller.getStatus().equals(StaticVar.sellerStatus.Approved)) {
                    products = productFacade.getAllProductsBySeller(seller);
                }else{
                    String errorMsg = "Your shop details has been sent for review, kindly wait for approval.";
                    request.getSession().setAttribute("errorMsg", errorMsg);
                    request.getSession().setAttribute("redirect", "redirect");
                    return;
                }
            }

        } else if (request.getAttribute("productList").equals("customer")) {
            products = productFacade.getAllProducts();
        }

        List<String> productImage = new ArrayList<>();
        if(products != null){
            for (Product product : products) {
                byte[] imageData = product.getProductImage();
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                productImage.add(base64Image);
            }
            request.setAttribute("products", products);
            request.setAttribute("images", productImage);
        }
    }
    private void getSellers(HttpServletRequest request, HttpServletResponse response) {
        List<Seller> sellers = sellerFacade.getAllSellers();
        if(sellers != null){
            request.setAttribute("sellers", sellers);
        }
    }

    private void getCustomers(HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customers = customerFacade.getAllCustomers();
        if(customers != null){
            request.setAttribute("customers", customers);
        }
    }

    private void getAdmins(HttpServletRequest request, HttpServletResponse response){
        List<Admin> admins = adminFacade.getAllAdmins();
        if(admins != null){
            request.setAttribute("admins",admins);
        }
    }

    public void getUsers(HttpServletRequest request, HttpServletResponse response){
        List<UserLog> users = userFacade.getAllUsers();
        if(users != null){
            request.setAttribute("users", users);
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
 