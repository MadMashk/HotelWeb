package hibernate;

import hibernate.sortings.ISortType;
import hibernate.sortings.SortingNavigator;
import model.GotServices;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GotServicesDao {
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;
    @Autowired
    private SortingNavigator sortingNavigator;

    public  void save(GotServices o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }


    public  void delete(GotServices o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }

    public List<GotServices> getAll(ISortType sortType) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<GotServices> gotServicesList = new ArrayList<>();
        if (sortType.toString().contains("ASC_SORT_BY_DATE")){
            gotServicesList=session.createQuery("from GotServices g order by g.date asc ").getResultList();
        }
        else if (sortType.toString().contains("DESC_SORT_BY_DATE")){
            gotServicesList=session.createQuery("from GotServices g order by g.date desc ").getResultList();
        }
        else if (sortType.toString().contains("ASC_SORT_BY_PRICE")){
            gotServicesList=session.createQuery("from GotServices  g order by  g.price asc ").getResultList();
        }
        else if (sortType.toString().contains("DESC_SORT_BY_PRICE")){
            gotServicesList=session.createQuery("from GotServices  g order by  g.price desc ").getResultList();
        }
        session.close();
        return gotServicesList;
    }
    public  List<GotServices> getAll() {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<GotServices> list= session.createQuery("from GotServices ").getResultList();
        session.close();
        return list;
    }
}
