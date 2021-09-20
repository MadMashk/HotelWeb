package hibernate;

import hibernate.sortings.ISortType;
import model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class RoomDao implements IDao<Room>{
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;

    @Override
    public  void save(Room o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }

    @Override
    public  void delete(Room o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }


    public Room getOne (int number) {
        return hibernateSessionFactoryUtil.getSessionFactory().openSession().get(Room.class, number);
    }

    @Override
    public List<Room> getAll(ISortType sortType) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Room> list= new ArrayList<>();
        if (sortType.toString().contains("ASC_CAPACITY_SORT")){
            list = session.createQuery("from Room order by capacity ASC ").getResultList();
        }
        else if(sortType.toString().contains("DESC_CAPACITY_SORT")) {
            list = session.createQuery("from Room order by capacity DESC ").getResultList();
        }
        else if(sortType.toString().contains("ASC_STARS_SORT")) {
            list =session.createQuery("from Room order by starsQuantity ASC ").getResultList();
        }
        else if(sortType.toString().contains("DESC_STARS_SORT")) {
            list =session.createQuery("from Room order by starsQuantity DESC ").getResultList();
        }
        else if (sortType.toString().contains("ASC_PRICE_SORT")) {
            list = session.createQuery("from Room order by price ASC ").getResultList();
        }
        else if (sortType.toString().contains("DESC_PRICE_SORT")) {
            list = session.createQuery("from Room order by price DESC ").getResultList();
        }
        session.close();
        return list;
    }

    @Override
    public  List<Room> getAll() {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Room> list= session.createQuery("from Room ").getResultList();
        session.close();
        return list;
    }
}
