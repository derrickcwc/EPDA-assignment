package com.example.shopoo.Facade;

import com.example.shopoo.MyEntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public abstract class AbstractFacade<T> {
    private Class<T> entityClass;
    protected EntityManager em;
    public AbstractFacade(Class<T> entityClass) {
        em = MyEntityManagerFactory.getInstance().createEntityManager();
        this.entityClass = entityClass;
    }
    public void create(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    public void edit(T entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    public void remove(T entity) {
        em.getTransaction().begin();
        em.remove(em.merge(entity));
        em.getTransaction().commit();
    }

    public T find(Object id) {
        return em.find(entityClass, id);
    }
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }
    public void closeEntityManager() {
        em.close();
    }
}
