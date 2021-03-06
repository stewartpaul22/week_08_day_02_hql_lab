package db;

import models.Child;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.hibernate.Transaction;
import org.hibernate.annotations.Sort;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.loader.custom.Return;

import java.util.List;

public class DBHelper {

    private static Transaction transaction;
    private static Session session;

    public static void save(Object object) {

        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // UPDATE
    public static void update(Object object) {

        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.update(object);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // List all of the children.
    public static <T> List<T> getAllChildren(Class classType) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<T> results = null;
        Criteria criteria = session.createCriteria(classType);
        results = getList(criteria);
        return results;
    }

    // Find a child by name
    public static <T> T findByName(Class classType, String name) {
        session = HibernateUtil.getSessionFactory().openSession();
        T result = null;

        Criteria cr = session.createCriteria(classType);
        cr.add(Restrictions.eq("name", name));
        result = (T)cr.uniqueResult();

        return result;
    }

    // Sort the children by Age.
    public static List<Child> orderByAge() {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Child> results = null;

        Criteria cr = session.createCriteria(Child.class);
        cr.addOrder(Order.asc("age"));
        results = cr.list();
        return results;
    }

    // Return only children with Soprano range.
    public static List<Child> childrenByVocalRange(String range) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Child> results = null;

        Criteria cr = session.createCriteria(Child.class);
        cr.add(Restrictions.eq("range", range));
        results = cr.list();

        return results;
    }

    // Return children under 10
    public static List<Child> childrenBelowAge(int age) {
        session = HibernateUtil.getSessionFactory().openSession();
        List<Child> results = null;

        Criteria cr = session.createCriteria(Child.class);
        cr.add(Restrictions.lt("age", age));
        results = cr.list();

        return results;
    }


    public static <T> List<T> getList(Criteria cr) {
        List<T> results = null;
        try {
            transaction = session.beginTransaction();
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static <T> T getUniqueResult(Criteria cr) {
        T result = null;
        try {
            transaction = session.beginTransaction();
            result = (T) cr.uniqueResult();
            transaction.commit();

        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

}
