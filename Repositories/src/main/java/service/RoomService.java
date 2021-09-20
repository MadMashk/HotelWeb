package service;

import exeptions.AlreadyExistsException;
import exeptions.InputException;
import exeptions.NotFoundException;
import hibernate.RentDao;
import hibernate.RoomDao;
import hibernate.sortings.SortingNavigator;
import lombok.Getter;
import model.Client;
import model.Rent;
import model.Room;
import model.constants.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RoomService {

@Autowired
@Getter
    private RoomDao dao;
@Autowired
 private RentDao rentDao;
    @Value("${quantityOfClients}")
    private String quantityOfClients;
    @Value("${changeRoomStatus}")
    private String changeRoomStatus;
    @Autowired
    private SortingNavigator sortingNavigator;
    private static final Logger logger = LogManager.getLogger(RoomService.class);
    public RoomService(){
    }

    public void setQuantityOfClients(String quantityOfClients) {
        this.quantityOfClients=quantityOfClients;

    }


    public Date reformat(String date) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.parse(date);
    }

    public List<Room> getFreeRooms(int sortIndex){
        List<Room> roomList = new ArrayList<>();
        for (int i = 0; i < sortingNavigator.roomSort(sortIndex).size(); i++) {
            if (sortingNavigator.roomSort(sortIndex).get(i).getStatus()== RoomStatus.FREE) {
                roomList.add(sortingNavigator.roomSort(sortIndex).get(i));
            }
        }
        return roomList;
    }

    public List<Room> getRooms(Integer sortIndex){
        return sortingNavigator.roomSort(sortIndex);
    }

    public Room addRoom(Room room) {
        roomAbsenceCheck(room.getNumber());
        dao.save(room);
        return room;
    }
     public Room updateRoom(int number, Room room){
        Room room1 = getRoom(number);
        priceCheck(room.getPrice(),room.getStarsQuantity(),room.getCapacity());


                if ( changeRoomStatus.equals("false")) {
                    System.out.println("you can not change the room status");
                    room.setStatus(room1.getStatus());
                }
                if (room.getStatus() == null) {
                    room.setStatus(room1.getStatus());
                }
                if (room.getCapacity() == null) {
                    room.setCapacity(room1.getCapacity());
                }
                if (room.getPrice() == null) {
                    room.setPrice(room1.getPrice());
                }
                if (room.getStarsQuantity() == null)   {
                    room.setStarsQuantity(room1.getStarsQuantity());
                }

                    dao.save(room);
                    return room;
    }


    public void deleteRoom(int number) {
        dao.delete(getRoom(number));
    }

   public Integer getFreeRoomQuantity(){ //количество свободных номеров
        int quantity=0;
       for (int i = 0; i < dao.getAll().size(); i++) {
           if (dao.getAll().get(i).getStatus() == RoomStatus.FREE) {
               quantity++;
           }
       }
       return quantity;
   }

    public void priceCheck(int priceOfNewRoom, int starsQuantityOfNewRoom, int capacityOfNewRoom) throws InputException{
        if (starsQuantityOfNewRoom==1 && priceOfNewRoom<(1000*capacityOfNewRoom) ||starsQuantityOfNewRoom==2 && priceOfNewRoom<(2000*capacityOfNewRoom) || starsQuantityOfNewRoom==3 && priceOfNewRoom<(3000*capacityOfNewRoom)){
            logger.error("wrong price");
            inputException();
        }
    }

    public Room getRoom(int number)  {
        Room room = dao.getOne(number);
            if (room==null) {
                logger.error("room not found");
                notFoundException();
            }
            return room;
    }

    public  void roomAbsenceCheck(int number)  { //проверка отсутствия комнаты
        Room room = dao.getOne(number);
        if(room!=null) {
            logger.error("such a room already exists");
            alreadyExistsException();
        }
    }



    public List<Room> checkFreeRoomsByDate( String searchDate, int dateRange ) throws ParseException { // проверка наличия комнаты по опредленному диапазону дат в будущем
        List<Rent> rentList =rentDao.getAll();
        List<Room> listRoom= dao.getAll();
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


    public List<Client> getLastClientsOfRoom(int roomNumber) { //последние  клиенты определенной комнаты
        List<Client> clientList=new ArrayList<>();
        Room room = getRoom(roomNumber);
        int number = 0;
        for (int i = rentDao.getAll().size() - 1; i >= 0 && number < Integer.parseInt(quantityOfClients); i--) {
            if (rentDao.getAll().get(i).getRoom().getNumber().equals(room.getNumber())) {
                clientList.add(rentDao.getAll().get(i).getClient());
                number++;
            }
        }
        return clientList;
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


