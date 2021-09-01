package service;

import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.IDao;
import model.Client;
import model.Rent;
import model.Room;
import model.constants.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class RoomService {
@Autowired
    private IDao dao;
    public RoomService(){
    }

    public void roomsByPriceComparator(List<Room> rooms){
        rooms.stream().sorted(Comparator.comparing(Room::getPrice).thenComparing(Room::getNumber)).collect(Collectors.toCollection(ArrayList<Room>::new));

    }
    public void roomsByStarsComparator(List<Room> rooms){
        rooms.stream().sorted(Comparator.comparing(Room::getStarsQuantity).thenComparing(Room::getNumber)).collect(Collectors.toCollection(ArrayList<Room>::new));
    }
    public void roomsByCapacityComparator(List<Room> rooms){
        rooms.stream().sorted(Comparator.comparing(Room::getCapacity).thenComparing(Room::getNumber)).collect(Collectors.toCollection(ArrayList<Room>::new));
    }
    public void printRentArrayList(List<Rent> rents) {
        for (Rent rent : rents) {
            System.out.println("Client " + rent.getClient() + " Arrival date " + rent.getArrivalDate() + " Departure date " + rent.getDepartureDate());
        }
    }
    public Date reformat(String date) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.parse(date);
    }

    public List<Room> getFreeRooms(){
        List<Room> roomList = new ArrayList<>();
        for (int i = 0; i < dao.getAll(Room.class).size(); i++) {
            if (dao.getAll(Room.class).get(i).getStatus()== RoomStatus.FREE) {
                roomList.add(dao.getAll(Room.class).get(i));
            }
        }
        return roomList;
    }
    public List<Room> getRooms(){
        return dao.getAll(Room.class);
    }

    public Room addOrSaveRoom(Room room) {
        dao.save(room);
        return room;
    }

    public void deleteRoom(int number) {
        for (int i = 0; i < dao.getAll(Room.class).size(); i++) {
            if (dao.getAll(Room.class).get(i).getNumber()==number) {
                dao.delete(dao.getAll(Room.class).get(i));
            }
        }

    }

   public Integer getFreeRoomQuantity(){ //количество свободных номеров
        int quantity=0;
       for (int i = 0; i < dao.getAll(Room.class).size(); i++) {
           if (dao.getAll(Room.class).get(i).getStatus() == RoomStatus.FREE) {
               quantity++;
           }
       }
       return quantity;
   }
    public boolean capacityCheck(int capacity) throws InputException {
        if (capacity<1 || capacity>7) {
            inputException();
        }
        return true;
    }
    public  boolean starsQuantityCheck(int starsQuantity) throws InputException {
        if (starsQuantity<1 || starsQuantity>3){
            inputException();
        }
        return true;
    }
    public boolean priceCheck(int priceOfNewRoom, int starsQuantityOfNewRoom, int capacityOfNewRoom) throws InputException{
        if (starsQuantityOfNewRoom==1 && priceOfNewRoom<(1000*capacityOfNewRoom) ||starsQuantityOfNewRoom==2 && priceOfNewRoom<(2000*capacityOfNewRoom) || starsQuantityOfNewRoom==3 && priceOfNewRoom<(3000*capacityOfNewRoom)){
            inputException();
        }
        return true;
    }

    public boolean roomCheck(int number) throws AlreadyExistsException {
        for (int i = 0; i < dao.getAll(Room.class).size(); i++) { //проверка наличия комнаты
            if (dao.getAll(Room.class).get(i).getNumber()==number){
                alreadyExistsException();
            }
        }
        return false;
    }
    public  boolean roomAbsenceCheck(int number) throws NotFoundException { //проверка отсутствия комнаты
        Stream<Room> stream = dao.getAll(Room.class).stream();
        boolean match = stream.anyMatch(room -> room.getNumber()==number);
        if(!match){
            notFoundException();
        }

        return match;
    }

    public Room getRoomPerNumber(int number){ //получение комнаты по номеру
        for (Room room : dao.getAll(Room.class)) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }


    public List<Room> checkFreeRoomsByDate( String searchDate, int dateRange ) throws ParseException { // проверка наличия комнаты по опредленному диапазону дат в будущем
        List<Rent> rentList =dao.getAll(Rent.class);
        List<Room> listRoom= dao.getAll(Room.class);
        List<Room> listFreeRoom = new ArrayList<>();
        Date todayDate = new Date();
        if (reformat(searchDate).compareTo(todayDate) > 0) {
            for (Rent rent : rentList) {
                long difference = rent.getDepartureDate().getTime() - rent.getArrivalDate().getTime();
                String a = String.format("%d", difference / 86400000);
                int result = Integer.parseInt(a);
                Date[] datesOfRooms = new Date[result];
                fullArr(rent.getArrivalDate(), datesOfRooms);
                Date searchDateD = reformat(searchDate);
                Date[] datesOfSearchRooms = new Date[dateRange];
                fullArr(searchDateD, datesOfSearchRooms);
                boolean exit = true;
                for (int k = 0; k < datesOfRooms.length && exit; k++) {
                    for (Date datesOfSearchRoom : datesOfSearchRooms) {
                        if (datesOfRooms[k].compareTo(datesOfSearchRoom) == 0) {
                            exit = false;
                            break;
                        }
                    }
                    if (k == datesOfRooms.length - 1)
                        listFreeRoom.add(rent.getRoom());
                }
            }
            for (Room room : listRoom) {
                if (!room.getRentStatus())
                    listFreeRoom.add(room);
            }
        }
        return listFreeRoom;
    }
    public void fullArr(Date date, Date[] datesOfRooms) {
        Calendar cal1 = GregorianCalendar.getInstance();
        for (int i = 0; i <= datesOfRooms.length - 1; i++) {
            cal1.setTime(date);
            cal1.add(GregorianCalendar.DAY_OF_MONTH, i);
            datesOfRooms[i] = cal1.getTime();

        }
    }

    public void changeStatusRoom(int roomNumber,RoomStatus status){ //изменить статус комнаты
        Room room=getRoomPerNumber(roomNumber);
        room.setStatus(status);
    }

    public List<Client> getLastClientsOfRoom(int roomNumber, int quantity) { //последние  клиенты определенной комнаты и их даты прибывания
        List<Client> clientList=null;
        int number = 0;
        for (int i = dao.getAll(Rent.class).size() - 1; i >= 0 && number < quantity; i--) {
            if (dao.getAll(Rent.class).get(i).getRoom().getNumber() == roomNumber) {
                clientList.add(dao.getAll(Rent.class).get(i).getClient());
                number++;
            }
        }
        return clientList;
    }

    public void changePrice(int roomNumber, int newPrice){ //изменение цены
        for (int i = 0; i < dao.getAll(Room.class).size(); i++) {
            if (dao.getAll(Room.class).get(i).getNumber()==roomNumber){
                dao.getAll(Room.class).get(i).setPrice(newPrice);
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


