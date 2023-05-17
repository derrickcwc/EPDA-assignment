package com.example.shopoo.Facade;

import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.Feedback;
import com.example.shopoo.Model.Feedback;
import com.example.shopoo.Model.Orders;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class FeedbackFacade extends AbstractFacade<Feedback> {
    public FeedbackFacade (){
        super(Feedback.class);
    }
    public void createFeedback(Feedback feedback){
        try{
            super.create(feedback);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editFeedback(Feedback feedback){
        try {
            super.edit(feedback);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFeedback(Feedback feedback){
        try {
            super.remove(feedback);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbacks = super.findAll();
        if (feedbacks.isEmpty()){
            return null;
        }
        return feedbacks;
    }

    public Feedback getFeedbackById(Long feedbackId) {
        if(super.find(feedbackId)== null){
            return null;
        }
        return super.find(feedbackId);
    }

    public Feedback getCustomerFeedbackByOrder(Orders orders){
        TypedQuery<Feedback> query = em.createQuery("SELECT c FROM Feedback c WHERE c.orders = :orders AND c.customer = c.orders.customer", Feedback.class);
        query.setParameter("orders", orders);
        List<Feedback> feedbacks = query.getResultList();
        if (feedbacks.isEmpty()) {
            return null; // Customer not found
        }
        return feedbacks.get(0);
    }
    public Feedback getSellerFeedbackByOrder(Orders orders){
        TypedQuery<Feedback> query = em.createQuery("SELECT c FROM Feedback c WHERE c.orders = :orders AND c.seller = c.orders.seller", Feedback.class);
        query.setParameter("orders", orders);
        List<Feedback> feedbacks = query.getResultList();
        if (feedbacks.isEmpty()) {
            return null; // Customer not found
        }
        return feedbacks.get(0);
    }

    public List<Feedback> getAllFeedbackByRating(){
        TypedQuery<Feedback> query =
                em.createQuery("SELECT c FROM Feedback c ORDER BY c.rating DESC", Feedback.class);
        List<Feedback> feedbacks = query.getResultList();
        if(feedbacks.isEmpty()){
            return null;
        }
        return feedbacks;
    }

    public Double getAverageRatingBySeller(Long sellerId){
        TypedQuery<Double> query =
                em.createQuery("SELECT AVG(f.rating) FROM Feedback f JOIN f.orders o WHERE o.seller.id = :sellerId", Double.class);
        query.setParameter("sellerId", sellerId);
        return query.getSingleResult();
    }
}
