package hibernate;

import java.util.List;

public interface IDao {
    public <T> void save(final T o);
    public <T> void update(final T o);
    public <T> void delete(final T o);
    public <T> List<T> getAll(final Class<T> type);
}
