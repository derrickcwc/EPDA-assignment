package com.example.shopoo.Facade;

import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.Seller;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    public ProductFacade (){
        super(Product.class);
    }
    public void createProduct(Product product){
        try{
            super.create(product);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editProduct(Product product){
        try {
            super.edit(product);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAllProducts() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.deleted = false", Product.class);
        List<Product> products = query.getResultList();
        if (products.isEmpty()) {
            return null; // Product not found
        }
        return products;
    }

    public List<Product> getAllProductsBySeller(Seller seller){
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.seller = :seller AND p.deleted = false",
                Product.class);
        query.setParameter("seller", seller);
        List<Product> products = query.getResultList();
        if (products.isEmpty()) {
            return null; // Product not found
        }
        return products;
    }

    public Product getProductById(Long productId) {
        if(super.find(productId)== null){
            return null;
        }
        return super.find(productId);
    }
}
