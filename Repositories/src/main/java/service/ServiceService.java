package service;

import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.IDao;
import model.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@org.springframework.stereotype.Service
public class ServiceService {
@Autowired
private IDao dao;
    public ServiceService() {

    }

     public boolean checkService(int index) throws AlreadyExistsException {
         for (int i = 0; i <dao.getAll(Service.class).size(); i++) {
             if(dao.getAll(Service.class).get(i).getIndex()==index){
                alreadyExistsException();
             }
         }
         return false;
     }
     public boolean checkServiceName(String name) throws InputException {
        if(name.trim().length()==0){
            inputException();
        }
        return true;
     }
     public boolean nullCheck(int number) throws  InputException{
        if(number<=0){
            inputException();
        }
        return false;
     }
     public boolean serviceAbsenceCheck( int index) throws NotFoundException {
         Stream<Service> stream =dao.getAll(Service.class).stream();
         boolean match = stream.anyMatch(service -> service.getIndex()==index);
         if(!match){
             notFoundException();
         }
         return match;
     }

     public void serviceByPriceComparator(List<Service> services){
         services.stream().sorted(Comparator.comparing(Service::getPrice).thenComparing(Service::getIndex)).collect(Collectors.toCollection(ArrayList<Service>::new));
     }
    public void serviceByIndexComparator(List<Service> services){
        services.stream().sorted(Comparator.comparing(Service::getIndex).thenComparing(Service::getPrice)).collect(Collectors.toCollection(ArrayList<Service>::new));
    }

    public List<Service> getAllServices(){
       return  dao.getAll(Service.class);
    }
    public Service  saveOrAddService(Service service){ //добавление новой услуги
        dao.save(service);
        return service;
    }
    public void changePrice(int index, int price){
        for (int i = 0; i < dao.getAll(Service.class).size(); i++) {
            if(dao.getAll(Service.class).get(i).getIndex()==index){
                dao.getAll(Service.class).get(i).setPrice(price);
            }

        }
    }

    public void alreadyExistsException() throws AlreadyExistsException {
        throw new AlreadyExistsException();
    }
    public void inputException() throws InputException {
        throw new InputException();
    }
    public void notFoundException()throws NotFoundException {
        throw new NotFoundException();
    }
}
