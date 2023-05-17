package com.example.shopoo.Controller;

import com.example.shopoo.Facade.CartFacade;
import com.example.shopoo.Facade.ProductFacade;
import com.example.shopoo.Facade.SellerFacade;
import com.example.shopoo.Model.Cart;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Seller;
import com.opencsv.CSVWriter;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DownloadReport", value = "/DownloadReport")
public class DownloadReport extends HttpServlet {

    @EJB
    private ProductFacade productFacade;
    @EJB
    private CartFacade cartFacade;

    public void init() {
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");

        // Get the list of data
        List<Product> products = productFacade.getAllProducts();

        // Create the CSV writer
        PrintWriter writer = response.getWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        // Write the header row
        csvWriter.writeNext(new String[]{"ProductID", "ProductName", "Description","SellerName","Price","isDeleted", "Total Sold"});

        // Write the data rows
        for (Product product : products) {

            // Calculate total sold of product
            List<Cart> carts = cartFacade.getSoldCartItemsByProductId(product.getId());

            int totalSales = 0;
            if(carts!= null){
                for(Cart cart : carts){
                    int quantity = cart.getQuantity();
                    totalSales += quantity;
                }
            }
            csvWriter.writeNext(new String[]{
                    String.valueOf(product.getId()),
                    product.getProductName(),
                    product.getDescription(),
                    String.valueOf(product.getSeller().getShopName()),
                    String.valueOf(product.getPrice()),
                    String.valueOf(product.getDeleted()),
                    String.valueOf(totalSales)
            });
        }

        // Close the CSV writer
        csvWriter.close();
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
 