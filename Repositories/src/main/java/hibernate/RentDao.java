package hibernate;

import hibernate.sortings.ISortType;
import hibernate.sortings.SortingNavigator;
import model.Rent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class RentDao implements IDao<Rent>{
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;
    @Autowired
    private SortingNavigator sortingNavigator;
    @Override
    public  void save(Rent o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }

    @Override
    public  void delete(Rent o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }
    @Override
    public List<Rent> getAll(ISortType sortType) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Rent> rentList = new ArrayList<>();
        if (sortType.toString().contains("ASC_SORT_BY_DEPARTURE_DATE")){
            rentList=session.createQuery("from Rent r order by r.departureDate asc ").getResultList();
        }
        else if (sortType.toString().contains("DESC_SORT_BY_DEPARTURE_DATE")){
            rentList=session.createQuery("from Rent r order by r.departureDate desc ").getResultList();
        }
        session.close();
        return rentList;
    }

    @Override
    public  List<Rent> getAll() {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Rent> list= session.createQuery("from Rent ").getResultList();
        session.close();
        return list;
    }
}
