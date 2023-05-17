package com.example.shopoo.Controller;

import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Facade.UserFacade;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Seller;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.UUID;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
@WebServlet(name = "ProductServlet", value = "/ProductServlet")
public class ProductServlet extends HttpServlet {

    @EJB
    private UserFacade userFacade;
    @EJB
    private SellerFacade sellerFacade;
    @EJB
    private ProductFacade productFacade;
    public void init() {

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
            //Process Request based on action
            switch (action){
                case "addProduct":
                    addProduct(request,response);
                    break;
                case "deleteProduct":
                    deleteProduct(request,response);
                    break;
                case "editProduct":
                    editProduct(request,response);
                    break;
                case "getProduct":
                case "viewProduct":
                    getProduct(request,response);
                    break;
            }
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        Product product = productFacade.getProductById(productId);

        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        Double price = Double.valueOf(request.getParameter("price"));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Double formattedPrice = Double.parseDouble(decimalFormat.format(price));

        product.setProductName(productName);
        product.setPrice(formattedPrice);
        product.setDescription(description);

        // Process image
        Part filePart = request.getPart("productImage");
        if (filePart != null && filePart.getSize()> 0) {
            InputStream inputStream = filePart.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            product.setProductImage(imageBytes);
        }

        productFacade.editProduct(product);

        String errorMsg = "Product Updated successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/sellerPage.jsp");
    }

    private void getProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        Product product = productFacade.getProductById(productId);

        request.setAttribute("getProduct", product);

        if(request.getParameter("action").equals("viewProduct")){
            byte[] imageData = product.getProductImage();
            String base64Image = Base64.getEncoder().encodeToString(imageData);

            request.setAttribute("productImage",base64Image);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/productDetails.jsp");
            dispatcher.forward(request,response);

        }else if(request.getParameter("action").equals("getProduct")){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/editProduct.jsp");
            dispatcher.forward(request,response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productId = Long.valueOf(request.getParameter("productId"));
        Product product = productFacade.getProductById(productId);
        product.setDeleted(true);
        productFacade.editProduct(product);

        String errorMsg = "Product Deleted successfully!";
        request.getSession().setAttribute("errorMsg", errorMsg);
        response.sendRedirect(request.getContextPath() + "/sellerPage.jsp");
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserLog user = (UserLog) request.getSession().getAttribute("user");
        Seller seller = sellerFacade.getSeller(user);

        if(seller != null){
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            Double price = Double.valueOf(request.getParameter("price"));

            Product product = new Product();
            product.setProductName(productName);
            product.setPrice(price);
            product.setDescription(description);
            product.setSeller(seller);
            product.setDeleted(false);

            // Process image
            Part filePart = request.getPart("productImage");
            if (filePart != null) {
                InputStream inputStream = filePart.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageBytes = outputStream.toByteArray();
                product.setProductImage(imageBytes);
            }

            productFacade.createProduct(product);

            String errorMsg = "Product Added successfully!";
            request.getSession().setAttribute("errorMsg", errorMsg);
            response.sendRedirect(request.getContextPath() + "/sellerPage.jsp");
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
 