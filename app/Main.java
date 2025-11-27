package app;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class Main {
    public static void main(String[] args) {
        // Point to hibernate.cfg.xml in project root
        Configuration cfg = new Configuration();
        cfg.configure(new java.io.File("hibernate.cfg.xml")); 
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            //Member member = new Member("Alice", "alice@example.com");
            //session.save(member);

            session.getTransaction().commit();
            System.out.println("Member saved!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}