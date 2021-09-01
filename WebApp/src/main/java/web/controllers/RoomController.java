package web.controllers;

import lombok.SneakyThrows;
import model.Client;
import model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.RoomService;

import java.util.List;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")   //список всех комнат
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @RequestMapping(value = "/free", method = RequestMethod.DELETE,headers = "Accept=application/json" )  //список свободных комнат
    public List<Room> getFreeRooms() {
        return roomService.getFreeRooms();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json")  //добавить комнаты
    public Room add(@RequestBody Room room) {
        return roomService.addOrSaveRoom(room);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,headers = "Accept=application/json" ) //обновить комнату
    public Room update(@RequestBody Room room) {
        return roomService.addOrSaveRoom(room);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE,headers = "Accept=application/json" )   //удалить комнату
    public void delete (@RequestParam("number") int number) {
        roomService.deleteRoom(number);
    }

    @RequestMapping(value = "/last-clients", method = RequestMethod.GET,headers = "Accept=application/json" ) //последние клиенты комнаты
    public List<Client> checkLastClient(@RequestParam("number") int number, @RequestParam("quantity") int quantity) {
        return roomService.getLastClientsOfRoom(number,quantity);
    }
    @SneakyThrows
    @RequestMapping(value = "/free-rooms-by-date", method = RequestMethod.GET,headers = "Accept=application/json" ) //последние клиенты комнаты
    public List<Room> getFreeRoomsByDate(@RequestParam("date") String date, @RequestParam("range") int range) {
        return roomService.checkFreeRoomsByDate(date,range);
    }
    @RequestMapping(value = "/quantity", method = RequestMethod.GET, headers ="Accept=application/json") //кол-во клиентов
     public Integer freeRoomsQuantity() {
        return roomService.getFreeRoomQuantity();
    }
    @RequestMapping(value = "/info", method = RequestMethod.GET) //информация
    public Room getInfo(@RequestParam ("number") int number) {
        return roomService.getRoomPerNumber(number);
    }

}
