package com.example.shopoo.Facade;

import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {

    public CustomerFacade (){
        super(Customer.class);
    }

    public void createCustomer(Customer cust){
        try{
            super.create(cust);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editCustomer(Customer cust){
        try {
            super.edit(cust);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCustomer(Customer cust){
        try {
            super.remove(cust);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Customer> getAllCustomers() {
        List<Customer> customers = super.findAll();
        if (customers.isEmpty()){
            return null;
        }
        return customers;
    }
    public Customer getCustomer(UserLog user){
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c WHERE c.user = :user", Customer.class);
        query.setParameter("user", user);
        List<Customer> customers = query.getResultList();
        if (customers.isEmpty()) {
            return null; // Customer not found
        }
        return customers.get(0);
    }

    public Customer getCustomerById(Long customerId) {
        if(super.find(customerId)== null){
            return null;
        }
        return super.find(customerId);
    }
}
