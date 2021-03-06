package hibernate;

import hibernate.sortings.ISortType;

import java.util.List;

public interface IDao<T> {

    List<T> getAll();

    List<T> getAll(ISortType sortType);

    void save(T t);

    void delete(T t);
}
