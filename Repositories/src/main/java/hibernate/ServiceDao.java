package hibernate;
import hibernate.sortings.ISortType;
import model.Service;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ServiceDao implements IDao<Service>{
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;

    @Override
    public  void save(Service o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }

    @Override
    public  void delete(Service o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }

    public Service getOne (int index) {
        return hibernateSessionFactoryUtil.getSessionFactory().openSession().get(Service.class, index);
    }

    @Override
    public List<Service> getAll(ISortType sortType) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Service> list= new ArrayList<>();
        if(sortType.toString().contains("ASC_SORT_BY_INDEX")) {
            list = session.createQuery("from Service s order by s.index asc ").getResultList();
        }
        else if(sortType.toString().contains("DESC_SORT_BY_INDEX")) {
            list = session.createQuery("from Service s order by s.index desc ").getResultList();
        }
        else if(sortType.toString().contains("ASC_SORT_BY_PRICE")) {
            list =session.createQuery("from Service order by price asc ").getResultList();
        }
        else if(sortType.toString().contains("DESC_SORT_BY_PRICE")) {
            list =session.createQuery("from Service order by price desc ").getResultList();
        }
        session.close();
        return list;
    }

    @Override
    public  List<Service> getAll() {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Service> list= session.createQuery("from Service ").getResultList();
        session.close();
        return list;
    }
}
