package service;

import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.IDao;
import model.*;
import model.constants.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@org.springframework.stereotype.Service
public class ClientService {
    @Autowired
    private IDao dao;

    public ClientService() {

    }

    public void rentsByDepartureDateComparator(List<Rent> rents) {
        rents.stream().sorted(Comparator.comparing(Rent::getDepartureDate).thenComparing(Rent::getClientName)).collect(Collectors.toCollection(ArrayList<Rent>::new));
    }

    public void rentsByClientNameComparator(List<Rent> rents) {
        rents.stream().sorted(Comparator.comparing(Rent::getClientName).thenComparing(Rent::getDepartureDate)).collect(Collectors.toCollection(ArrayList<Rent>::new));
    }

    public List<GotServices> gotServicesByServiceNameComparator(List<GotServices> gotServices) {
        return gotServices.stream().sorted(Comparator.comparing(GotServices::getNameOfService)).collect(Collectors.toCollection(ArrayList<GotServices>::new));

    }

    public void gotServicesByPriceComparator(List<GotServices> gotServices) {
        gotServices.stream().sorted(Comparator.comparing(GotServices::getPrice).thenComparing(GotServices::getDate)).collect(Collectors.toCollection(ArrayList<GotServices>::new));
    }

    public void gotServicesByDateComparator(List<GotServices> gotServices) {
        gotServices.stream().sorted(Comparator.comparing(GotServices::getDate).thenComparing(GotServices::getPrice)).collect(Collectors.toCollection(ArrayList<GotServices>::new));

    }



    public Client addClient(Client client) {
        dao.save(client);
        return client;
    }

    public Client updateClient(Client client) {
        dao.update(client);
        return client;
    }

    public void deleteClient(String pass) {
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            if (dao.getAll(Client.class).get(i).getPassportNumber().equals(pass)) {
                dao.delete(dao.getAll(Client.class).get(i));
            }
        }

    }
    public List<Client> getAllClients() {
        return dao.getAll(Client.class);
    }

    public boolean checkClient(String pass) throws AlreadyExistsException { //проверка наличия номера паспорта
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            if (dao.getAll(Client.class).get(i).getPassportNumber().equals(pass)) {
                alreadyExistsException();
            }
        }
        return false;

    }

    public boolean checkPassAbsence(String pass) throws NotFoundException {// Проверка отсутствия паспорта
        Stream<Client> stream = dao.getAll(Client.class).stream();
        boolean match = stream.anyMatch(client -> client.getPassportNumber().equals(pass));
        if (!match) {
            notFoundException();
        }

        return match;
    }

    public boolean checkPhoneOfClient(String phone) throws AlreadyExistsException { //проверка наличия номера телефона
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            if (dao.getAll(Client.class).get(i).getPhoneNumber().equals(phone)) {
                alreadyExistsException();
            }
        }
        return false;
    }

    public boolean checkDate(String date) throws InputException {
        String datePattern;
        datePattern = "\\d{2}-\\d{2}-\\d{4}";
        if (!date.matches(datePattern)) {
            inputException();
        }
        return true;
    }

    public boolean checkDuration(int duration) throws InputException {
        if (duration < 1) {
            inputException();
        }
        return true;
    }

    public Client getClientByPass(String pass) {
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            if (dao.getAll(Client.class).get(i).getPassportNumber().equals(pass)) {
                return dao.getAll(Client.class).get(i);
            }
        }
        return new Client();
    }

    public boolean roomCheck(int number) {
        for (int i = 0; i < dao.getAll(Room.class).size(); i++) {
            if (dao.getAll(Room.class).get(i).getNumber() == number) {
                return true;
            }
        }
        return false;
    }

    public Room getRoomPerNumber(int number) { //получение комнаты по номеру
        for (Room room : dao.getAll(Room.class)) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return new Room();
    }

    public Boolean checkService(int index) {
        for (int i = 0; i < dao.getAll(Service.class).size(); i++) {
            if (dao.getAll(Service.class).get(i).getIndex() == index) {
                return true;
            }
        }
        return false;
    }

    public Service getServicePerIndex(int serviceIndex) {

        for (int i = 0; i < dao.getAll(Service.class).size(); i++) {
            if (dao.getAll(Service.class).get(i).getIndex() == serviceIndex) {
                return dao.getAll(Service.class).get(i);
            }
        }
        return new Service();
    }

    public void setService(int index, String pass) { //добавление услуги клиенту
        Service service = getServicePerIndex(index);
        Client client = getClientByPass(pass);
        GotServices gotServices = new GotServices();
        gotServices.setService(service);
        Date todayDate = new Date();
        gotServices.setDate(todayDate);
        gotServices.setClient(client);
        gotServices.setNameOfService(gotServices.getService().getName());
        gotServices.setPrice(service.getPrice());
        dao.getAll(GotServices.class).add(gotServices);
    }

    public Date reformat(String date) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.parse(date);
    }

    public Integer getPriceOfRent(String arrivalDate, String clientPass) throws ParseException { //вывод цены съемки комнаты определенного клиента по определенной дате
        List<Rent> rents = dao.getAll(Rent.class);
        Date arrivalDateD = reformat(arrivalDate);
        for (Rent rent : rents) {
            if (rent.getArrivalDate().compareTo(arrivalDateD) == 0 && rent.getClient().getPassportNumber().equals(clientPass)) {
                return rent.getPrice();
            }
        }
        return 0;
    }

    public void setRoom(String clientPass, int roomNumber, String arrivalDate, int stayDuration) throws ParseException{ //добавление клиенту комнаты
        Room room = getRoomPerNumber(roomNumber);
        Client client = getClientByPass(clientPass);
        client.setRoom(room);
        room.setStatus(RoomStatus.RESERVED);
        Rent rent = new Rent();
        rent.setClient(client);
        rent.setRoom(room);
        rent.setDates(arrivalDate, stayDuration);
        rent.getPrice();
        room.setRentStatus(true);
        dao.save(rent);
        dao.save(room);

    }

    public void unsetRoom(String clientPass){ //выселить из комнаты
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            if(dao.getAll(Client.class).get(i).getPassportNumber().equals(clientPass)){
                dao.getAll(Client.class).get(i).setRoom(null);
            }

        }
    }
    public ArrayList<GotServices> listGotServicesOfClient(String clientPass) { //лист услуг определенного клиента
        List<GotServices> list = dao.getAll(GotServices.class);
       ArrayList<GotServices> gotServicesOfThisClient = new ArrayList<>();
        for (GotServices gotServices : list) {
            if (gotServices.getClient().getPassportNumber().equals(clientPass)) {
                gotServicesOfThisClient.add(gotServices);
            }
        }
       return gotServicesOfThisClient;

    }
   public int totalPriceOfServices(String clientPass){ //общая стоимость услуг определенного клиента
       List<GotServices> gotServices= dao.getAll(GotServices.class);
        int totalPrice =0;
       for (GotServices gotService : gotServices) {
           if (gotService.getClient().getPassportNumber().equals(clientPass)) {
               totalPrice += gotService.getService().getPrice();
           }
       }
       return totalPrice;
   }

    public int getQuantityOfClients(){ //общее число клиентов
        int quantity=0;
        for (int i = 0; i < dao.getAll(Client.class).size(); i++) {
            quantity++;
        }
        return quantity;
    }


    public boolean nameCheck(String name) throws InputException { //проверка на правильность имени
     if (name.trim().length()==0 || !name.toLowerCase(Locale.ROOT).matches(("[a-z]+"))) {
         inputException();
     }
            return true;
    }

    public boolean passCheck(String pass) throws InputException {
        if (pass.trim().length() != 9) {
            inputException();
        }
            return true;
    }
  public boolean phoneCheck(String phone) throws  InputException{
        if (phone.trim().length() != 11){
            inputException();
        }
            return true;
  }
   public String clientInfo(String pass){
        List<Client> list = dao.getAll(Client.class);
        for (Client client : dao.getAll(Client.class)){
            if (client.getPassportNumber().equals(pass)) {
                return client.getName() + client.getPassportNumber() + client.getPassportNumber();
            }
        }
        return "no such client";
   }
    public void alreadyExistsException() throws AlreadyExistsException{
        throw new AlreadyExistsException();
    }
    public void inputException() throws InputException {
        throw new InputException();
    }
    public void notFoundException()throws NotFoundException{
        throw new NotFoundException();
    }



}


