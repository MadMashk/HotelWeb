package service;
import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.*;
import hibernate.sortings.SortingNavigator;
import lombok.Getter;
import model.*;
import model.constants.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@org.springframework.stereotype.Service
public class ClientService {
    @Getter
    @Autowired
    private ClientDao dao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private GotServicesDao gotServicesDao;
    @Autowired
    private RentDao rentDao;
    @Autowired
    private SortingNavigator sortingNavigator;

    private static final Logger logger = LogManager.getLogger(ClientService.class);
    public ClientService() {

    }


    public Client addClient(Client client) {
        nameCheck(client.getName());
        passCheck(client.getPassportNumber());
        phoneCheck(client.getPhoneNumber());
        if (dao.getOne(client.getPassportNumber()) != null) {
            logger.error("such client already exist");
            alreadyExistsException();
        }
        dao.save(client);
        return client;
    }

    public Client updateClient(String pass, Client client) {
        Client client1 = getClient(pass);
        if (client.getPassportNumber() == null) {
            client.setPassportNumber(client1.getPassportNumber());
        }
        if (nameCheck(client.getName())) {
            if (client.getName() == null) {
                client.setName(client1.getName());
            }
        }
        if (client.getPhoneNumber() == null) {
            client.setPhoneNumber(client1.getPhoneNumber());
        }
        dao.save(client);
        return client;
    }

    public void deleteClient(String pass) {
        dao.delete(getClient(pass));
    }

    public Client getClient(String pass) {
        Client client = dao.getOne(pass);
        if (client == null) {
            logger.error("A client with pass " + pass + " doesn't exist");
            notFoundException();
        }
        return client;
    }

    public List < Client > getAllClients(Integer sortType) { //все клиенты
        if (sortType == 3) {
            List < Client > clientList = new ArrayList < > ();
            for (int i = sortingNavigator.rentSort(1).size() - 1; i > 0; i--) { //сортировка по дате отъезда
                if (!clientList.contains(sortingNavigator.rentSort(1).get(i).getClient()) && clientList.size() != sortingNavigator.clientSort(1).size()) {
                    clientList.add(sortingNavigator.rentSort(1).get(i).getClient());
                }
            }
            return clientList;
        }
        return sortingNavigator.clientSort(sortType);

    }


    public void checkDate(String date) throws InputException {
        String datePattern;
        datePattern = "\\d{2}-\\d{2}-\\d{4}";
        if (!date.matches(datePattern)) {
            logger.error("wrong date");
            inputException();
        }
    }


    public void checkDuration(int duration) throws InputException {
        if (duration < 1) {
            logger.error("wrong duration");
            inputException();
        }
    }


    public void setService(int index, String pass) { //добавление услуги клиенту
        Service service = serviceDao.getOne(index);
        Client client = dao.getOne(pass);
        GotServices gotServices = new GotServices();
        gotServices.setService(service);
        Date todayDate = new Date();
        gotServices.setDate(todayDate);
        gotServices.setClient(client);
        gotServices.setNameOfService(gotServices.getService().getName());
        gotServices.setPrice(service.getPrice());
        gotServicesDao.save(gotServices);
    }


    public Date reformat(String date) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.parse(date);
    }


    public Integer getPriceOfRent(String arrivalDate, String clientPass) throws ParseException { //вывод цены съемки комнаты определенного клиента по определенной дате
        Client client = getClient(clientPass);
        List < Rent > rents = rentDao.getAll();
        Date arrivalDateD = reformat(arrivalDate);
        for (Rent rent: rents) {
            if (rent.getArrivalDate().compareTo(arrivalDateD) == 0 && rent.getClient().getPassportNumber().equals(client.getPassportNumber())) {
                return rent.getPrice();
            }
        }
        return 0;
    }


    public void setRoom(String clientPass, int roomNumber, String arrivalDate, int stayDuration) throws ParseException { //добавление клиенту комнаты
        checkDuration(stayDuration);
        checkDate(arrivalDate);
        Room room = roomDao.getOne(roomNumber);
        Client client = getClient(clientPass);
        client.setRoom(room);
        room.setStatus(RoomStatus.RESERVED);
        Rent rent = new Rent();
        rent.setClient(client);
        rent.setRoom(room);
        rent.setDates(arrivalDate, stayDuration);
        rent.getPrice();
        room.setRentStatus(true);
        rentDao.save(rent);
        roomDao.save(room);
        dao.save(client);
    }


    public void unsetRoom(String clientPass) { //выселить из комнаты
        getClient(clientPass).setRoom(null);
    }


    public ArrayList < GotServices > listGotServicesOfClient(String clientPass, int sortIndex) { //лист услуг определенного клиента
        Client client = getClient(clientPass);
        List < GotServices > list = sortingNavigator.gotServicesSort(sortIndex);
        ArrayList < GotServices > gotServicesOfThisClient = new ArrayList < > ();
        for (GotServices gotServices: list) {
            if (gotServices.getClient().getPassportNumber().equals(client.getPassportNumber())) {
                gotServicesOfThisClient.add(gotServices);
            }
        }
        return gotServicesOfThisClient;

    }


    public int totalPriceOfServices(String clientPass) { //общая стоимость услуг определенного клиента
        Client client = getClient(clientPass);
        List < GotServices > gotServices = gotServicesDao.getAll();
        int totalPrice = 0;
        for (GotServices gotService: gotServices) {
            if (gotService.getClient().getPassportNumber().equals(client.getPassportNumber())) {
                totalPrice += gotService.getService().getPrice();
            }
        }
        return totalPrice;
    }


    public int getQuantityOfClients() { //общее число клиентов
        int quantity = 0;
        for (int i = 0; i < dao.getAll().size(); i++) {
            quantity++;
        }
        return quantity;
    }


    public boolean nameCheck(String name) throws InputException { //проверка на правильность имени
        if (name.trim().length() == 0 || !name.toLowerCase(Locale.ROOT).matches(("[a-z]+"))) {
            logger.error("the name is incorrect");
            inputException();
        }
        return true;
    }


    public void passCheck(String pass) throws InputException {
        if (pass.trim().length() != 9) {
            logger.error("pass must be 9 digits");
            inputException();
        }

    }


    public void phoneCheck(String phone) throws InputException {
        for (int i = 0; i < dao.getAll().size(); i++) {
            if (dao.getAll().get(i).getPhoneNumber().equals(phone)) {
                logger.error("client with such a phone number already exists");
                alreadyExistsException();
            }
        }
        if (phone.trim().length() != 11) {
            logger.error("phone must be 11 digits");
            inputException();
        }
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


