package com.example.shopoo.Facade;

import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.Orders;
import com.example.shopoo.Model.Payment;
import com.example.shopoo.Model.Seller;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class OrderFacade extends AbstractFacade<Orders> {
    public OrderFacade (){
        super(Orders.class);
    }
    public void createOrder(Orders order){
        try{
            super.create(order);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editOrder(Orders order){
        try {
            super.edit(order);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrder(Orders order){
        try {
            super.remove(order);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Orders> getAllOrders() {
        List<Orders> orders = super.findAll();
        if (orders.isEmpty()){
            return null;
        }
        return orders;
    }

    public Orders getOrderById(Long orderId) {
        if(super.find(orderId)== null){
            return null;
        }
        return super.find(orderId);
    }

    public List<Orders> getAllOrdersByCustomer(Customer customer) {
        TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o WHERE o.customer = :customer ORDER BY o.orderDate DESC ",
                Orders.class);
        query.setParameter("customer", customer);
        List<Orders> customers = query.getResultList();
        if (customers.isEmpty()) {
            return null; // Customer not found
        }
        return customers;
    }

    public List<Orders> getAllOrdersBySeller(Seller seller) {
        TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o WHERE o.seller = :seller ORDER BY o.orderDate DESC ",
                Orders.class);
        query.setParameter("seller", seller);
        List<Orders> sellers = query.getResultList();
        if (sellers.isEmpty()) {
            return null; // Customer not found
        }
        return sellers;
    }
}
