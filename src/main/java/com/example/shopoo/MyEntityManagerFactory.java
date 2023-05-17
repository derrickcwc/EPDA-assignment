package com.example.shopoo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MyEntityManagerFactory {
    private static MyEntityManagerFactory instance;
    private EntityManagerFactory emf;

    private MyEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("postgres");
    }

    public static synchronized MyEntityManagerFactory getInstance() {
        if (instance == null) {
            instance = new MyEntityManagerFactory();
        }
        return instance;
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        emf.close();
    }
}
