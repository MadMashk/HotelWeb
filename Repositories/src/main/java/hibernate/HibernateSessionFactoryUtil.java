package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactoryUtil {
    public  SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    private  SessionFactory sessionFactory= buildSessionFactory();
    public HibernateSessionFactoryUtil(){
    }
    public   SessionFactory buildSessionFactory() {
        try {
    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex){
            System.err.println("creation failed"+ex);
            throw  new ExceptionInInitializerError(ex);
        }
    }
}