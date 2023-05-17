package com.example.shopoo.Facade;

import com.example.shopoo.Model.Admin;
import com.example.shopoo.Model.Customer;
import com.example.shopoo.Model.UserLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class AdminFacade extends AbstractFacade<Admin> {

    public AdminFacade (){
        super(Admin.class);
    }

    public void createAdmin(Admin admin){
        try{
            super.create(admin);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void editAdmin(Admin admin){
        try{
            super.edit(admin);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void deleteAdmin(Admin admin){
        try {
            super.remove(admin);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Admin getAdminByStaffId(String staffId){
        TypedQuery<Admin> query = em.createQuery("SELECT admin FROM Admin admin WHERE admin.staffId = :staffId", Admin.class);
        query.setParameter("staffId", staffId);
        List<Admin>admins = query.getResultList();
        if (admins.isEmpty()) {
            return null; // Admin not found
        }
        return admins.get(0);
    }
    public Admin getAdminById(Long adminId){
        if(super.find(adminId)== null){
            return null;
        }
        return super.find(adminId);
    }
    public List<Admin> getAllAdmins() {
        List<Admin> admins = super.findAll();
        if (admins.isEmpty()){
            return null;
        }
        return admins;
    }
}
