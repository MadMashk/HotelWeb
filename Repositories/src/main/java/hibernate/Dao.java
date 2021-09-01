package hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Component
public class Dao implements IDao{
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;
    public Dao(){
    }
    @Override
    public <T> void save(final T o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }
    @Override
    public <T> List<T> getAll(final Class<T> type) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> list = session.createQuery(criteria).getResultList();;
        session.close();
        return list;
        }

    @Override
    public <T> void delete(T o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }
    @Override
    public <T> void update(final T o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(o);
        tx.commit();
        session.close();
    }
}
