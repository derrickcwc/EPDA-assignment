package com.example.shopoo.Facade;

import com.example.shopoo.Model.Cart;
import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.Seller;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CartFacade extends AbstractFacade<Cart> {
    public CartFacade (){
        super(Cart.class);
    }

    public void createCart(Cart cart){
        try{
            super.create(cart);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editCart(Cart cart){
        try {
            super.edit(cart);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCart(Cart cart){
        try {
            super.remove(cart);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Cart> getAllCartItems(Customer customer) {
        TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c WHERE c.isCheckout = false AND c.customer = :customer ORDER BY c.product.seller.id ASC",
                Cart.class);
        query.setParameter("customer", customer);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // cart not found
        }
        return carts;
    }
    public List<Seller> getAllCartSellers(Customer customer) {
        TypedQuery<Seller> query = em.createQuery(
                "SELECT DISTINCT c.product.seller FROM Cart c WHERE c.isCheckout = false AND c.customer = :customer ORDER BY c.product.seller.id ASC",
                Seller.class);
        query.setParameter("customer", customer);
        List<Seller> cartSeller = query.getResultList();
        if (cartSeller.isEmpty()) {
            return null; // cart not found
        }
        return cartSeller;
    }
    public List<Cart> getUnpaidCartsBySeller (Seller seller, Customer customer){
        TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c " +
                        "WHERE c.isCheckout = false AND c.customer = :customer AND c.product.seller = :seller",
                Cart.class);
        query.setParameter("customer", customer);
        query.setParameter("seller", seller);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // cart not found
        }
        return carts;
    }
    public List<Cart> getPurchaseHistoryByCustomer(Customer customer){
        TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c " +
                        "WHERE c.isCheckout = true AND c.customer = :customer ORDER BY c.order.orderDate DESC",
                Cart.class);
        query.setParameter("customer", customer);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // cart not found
        }
        return carts;
    }

    public List<Cart> getSalesBySeller(Seller seller) {
        TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c " +
                        "WHERE c.isCheckout = true AND c.product.seller = :seller ORDER BY c.order.orderDate DESC",
                Cart.class);
        query.setParameter("seller", seller);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // cart not found
        }
        return carts;
    }

    public Cart getCartById(Long CartId) {
        if(super.find(CartId)== null){
            return null;
        }
        return super.find(CartId);
    }
    public List<Cart> getCartByOrderId(Long orderId) {
        TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c WHERE c.order.id = :orderId", Cart.class);
        query.setParameter("orderId", orderId);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // Customer not found
        }
        return carts;
    }

    public List<Cart> getSoldCartItemsByProductId(Long productId){
        TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c WHERE c.product.id = :productId AND c.isCheckout = true", Cart.class);
        query.setParameter("productId", productId);
        List<Cart> carts = query.getResultList();
        if (carts.isEmpty()) {
            return null; // Customer not found
        }
        return carts;
    }
}
