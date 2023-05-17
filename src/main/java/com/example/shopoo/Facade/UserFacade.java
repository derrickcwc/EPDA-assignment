package com.example.shopoo.Facade;

import com.example.shopoo.Model.UserLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;


@Stateless
public class UserFacade extends AbstractFacade<UserLog>{

    public UserFacade() {
        super(UserLog.class);
    }
    public void createUser(UserLog user){
        try{
            super.create(user);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserLog login(String email, String password) {
        UserLog user = getUserByEmail(email);
        if(user == null){
            return null;
        }
        if (password.equals(user.getPassword())) {
            return user; // Passwords match, return the user
        } else {
            return null; // Passwords don't match
        }
    }
    public void editUser(UserLog user){
        try {
            super.edit(user);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteUser(UserLog user){
        try {
            super.remove(user);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserLog getUserByEmail(String email){
        TypedQuery<UserLog> query = em.createQuery("SELECT u FROM UserLog u WHERE u.email = :email", UserLog.class);
        query.setParameter("email", email);
        List<UserLog> users = query.getResultList();
        if (users.isEmpty()) {
            return null; // UserLog not found
        }
        return users.get(0);
    }
    public List<UserLog> getAllUsers(){
        List<UserLog> users = super.findAll();
        if (users.isEmpty()){
            return null;
        }
        return users;
    }

    public UserLog getUserById(Long id) {
        if(super.find(id)== null){
            return null;
        }
        return super.find(id);
    }
}
