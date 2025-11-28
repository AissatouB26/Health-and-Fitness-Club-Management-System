package app;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Member;
import models.Trainer;


public class Main {
    public static void main(String[] args) {
        // Point to hibernate.cfg.xml in project root
        Configuration cfg = new Configuration();
        cfg.configure(new java.io.File("hibernate.cfg.xml")); 
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Member harsimran = new Member("Harsimran", 19, 130, "harsimran@example.com");
            Member maria = new Member("Maria", 20, 130, "maria@example.com");
            Trainer nat = new Trainer("Nat", "nat@example.com");
            //session.save();

            session.getTransaction().commit();
            System.out.println("Member saved!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}