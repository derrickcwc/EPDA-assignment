package com.example.shopoo.Facade;

import com.example.shopoo.Model.Seller;
import com.example.shopoo.Model.Seller;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class SellerFacade extends AbstractFacade<Seller> {

    public SellerFacade (){
        super(Seller.class);
    }

    public void createSeller(Seller seller){
        try{
            super.create(seller);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void editSeller(Seller seller){
        try{
            super.edit(seller);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void deleteSeller(Seller seller){
        try {
            super.remove(seller);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Seller getSeller(UserLog user){
        TypedQuery<Seller> query = em.createQuery("SELECT s FROM Seller s WHERE s.user = :user", Seller.class);
        query.setParameter("user", user);
        List<Seller> sellers = query.getResultList();
        if (sellers.isEmpty()) {
            return null; // Seller not found
        }
        return sellers.get(0);
    }
    public Seller getSellerById(Long sellerId){
        if(super.find(sellerId)== null){
            return null;
        }
        return super.find(sellerId);
    }
    public List<Seller> getAllSellers() {
        List<Seller> sellers = super.findAll();
        if (sellers.isEmpty()){
            return null;
        }
        return sellers;
    }
}
