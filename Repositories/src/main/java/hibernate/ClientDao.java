package hibernate;
import hibernate.sortings.ISortType;
import hibernate.sortings.SortingNavigator;
import model.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClientDao  implements IDao<Client> {
    @Autowired
    private HibernateSessionFactoryUtil hibernateSessionFactoryUtil;
    @Autowired
    private SortingNavigator sortingNavigator;
    @Override
    public  void save(Client o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(o);
        tx.commit();
        session.close();
    }


    @Override
    public  void delete(Client o) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(o);
        tx.commit();
        session.close();
    }


    public Client getOne (String passNumber) {
        return hibernateSessionFactoryUtil.getSessionFactory().openSession().get(Client.class, passNumber);
    }


    @Override
    public  List<Client> getAll(ISortType sortType) {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Client> list= new ArrayList<>();
        if (sortType.toString().contains("ASC_BY_NAME")){
            list=session.createQuery("from Client c order by c.name ASC ").getResultList();
        }
        else if (sortType.toString().contains("DESC_BY_NAME")){
            list=session.createQuery("from Client c order by c.name DESC ").getResultList();
        }

        session.close();
        return list;
    }


    @Override
    public  List<Client> getAll() {
        Session session = hibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Client> list= session.createQuery("from Client").getResultList();
        session.close();
        return list;
    }

}
