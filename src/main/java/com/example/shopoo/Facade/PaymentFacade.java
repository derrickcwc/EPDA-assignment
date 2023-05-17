package com.example.shopoo.Facade;

import com.example.shopoo.Facade.AbstractFacade;
import com.example.shopoo.Model.Payment;
import com.example.shopoo.Model.Product;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class PaymentFacade extends AbstractFacade<Payment> {
    public PaymentFacade (){
        super(Payment.class);
    }

    public void createPayment(Payment payment){
        try{
            super.create(payment);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editPayment(Payment payment){
        try {
            super.edit(payment);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePayment(Payment payment){
        try {
            super.remove(payment);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Payment> getAllPayments() {
        List<Payment> payments = super.findAll();
        if (payments.isEmpty()){
            return null;
        }
        return payments;
    }
    public Payment getPaymentById(Long paymentId) {
        if(super.find(paymentId)== null){
            return null;
        }
        return super.find(paymentId);
    }
}
