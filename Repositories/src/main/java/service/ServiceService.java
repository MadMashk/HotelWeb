package service;
import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.ServiceDao;
import hibernate.sortings.SortingNavigator;
import lombok.Getter;
import model.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
@org.springframework.stereotype.Service

public class ServiceService {
    @Autowired
    @Getter
    private ServiceDao dao;
    @Autowired
    private SortingNavigator sortingNavigator;
    private static final Logger logger = LogManager.getLogger(RoomService.class);
    public ServiceService() {

    }

    public void checkService(int index) throws AlreadyExistsException {
        Service service = dao.getOne(index);
        if (service != null) {
            logger.error("service already exists");
            alreadyExistsException();
        }
    }


    public void checkServiceName(String name) throws InputException {
        if (name.trim().length() == 0) {
            logger.error("wrong name");
            inputException();
        }
    }


    public Service getService(int index) {
        Service service = dao.getOne(index);
        if (service == null) {
            logger.error("service not found");
            notFoundException();
        }
        return service;
    }


    public List < Service > getAllServices(Integer sortIndex) {
        return sortingNavigator.serviceSort(sortIndex);
    }
    public Service addService(Service service) { //добавление новой услуги
        checkService(service.getIndex());
        checkServiceName(service.getName());

        dao.save(service);
        return service;
    }


    public Service update(int index, Service service) {
        Service service1 = getService(index);


        if (service.getPrice() == null) {
            service.setPrice(service1.getPrice());
        }

        if (service.getName() == null) {
            service.setName(service1.getName());
        }

        if (service.getIndex() == null) {
            service.setIndex(service1.getIndex());
        }
        dao.save(service);
        return service;

    }


    public void deleteService(int index) {
        dao.delete(getService(index));
    }


    public void alreadyExistsException() throws AlreadyExistsException {
        throw new AlreadyExistsException();
    }


    public void inputException() throws InputException {
        throw new InputException();
    }


    public void notFoundException() throws NotFoundException {
        throw new NotFoundException();
    }
}

