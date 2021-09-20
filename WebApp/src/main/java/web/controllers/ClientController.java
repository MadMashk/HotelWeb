package web.controllers;

import lombok.SneakyThrows;
import model.Client;
import model.GotServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ClientService;

import java.util.List;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;



    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")   //получение списка клиентов
    public List<Client> getClients(@RequestParam("sortType") Integer sortType) {
        return clientService.getAllClients(sortType);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json")  //добавление клиента
    public Client add(@RequestBody Client client) {
        return clientService.addClient(client);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT,headers = "Accept=application/json" )//обновить клиента
    public Client update(@RequestParam("pass") String pass, @RequestBody Client client) {
            return clientService.updateClient(pass,client);
    }
    @RequestMapping(value = "/", method = RequestMethod.DELETE,headers = "Accept=application/json" )   //удалить клиента
    public void delete (@RequestParam("pass") String pass) {
        clientService.deleteClient(pass);
    }

    @RequestMapping(value = "/quantity", method = RequestMethod.GET, headers = "Accept=application/json")   //кол-во
    public int allClients() {
        return clientService.getQuantityOfClients();
    }

    @SneakyThrows
    @RequestMapping(value = "/rent", method = RequestMethod.GET, headers = "Accept=application/json")   //цена съемки клиента по дате
    public Integer getPriceOfRent(@RequestParam("date") String date, @RequestParam("passNum") String passNum) {
            return clientService.getPriceOfRent(date, passNum);
    }

    @SneakyThrows
    @RequestMapping(value = "/setRoom", method = RequestMethod.POST)    // добавление комнаты клиенту
    public void setRoom(@RequestParam("roomNumber") int roomNumber, @RequestParam("pass") String pass, @RequestParam("date") String date, @RequestParam("duration") int duration){

        clientService.setRoom(pass,roomNumber,date,duration);
    }

    @RequestMapping(value = "/setService", method = RequestMethod.POST)     //добавление услуги клиенту
    public void setService(@RequestParam("serviceIndex") int serviceIndex, @RequestParam("pass") String pass){
        clientService.setService(serviceIndex,pass);
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET, headers = "Accept=application/json")   //полученные услуги
    public List<GotServices> getServicesList(@RequestParam("pass") String pass, @RequestParam int sortIndex){
        return clientService.listGotServicesOfClient(pass,sortIndex);
    }

    @RequestMapping(value = "/services/total-price", method = RequestMethod.GET, headers = "Accept=application/json")   //общая цена услуг
    public int getTotalPrice(@RequestParam("pass") String pass){
        return clientService.totalPriceOfServices(pass);
    }

    @RequestMapping(value = "/unsetRoom", method = RequestMethod.POST)  //выселить из комнаты
    public void unsetRoom( @RequestParam("pass") String pass){
        clientService.unsetRoom(pass);
    }


    @RequestMapping(value = "/info", method = RequestMethod.GET) //информация
    public Client getInfo(@RequestParam ("pass") String pass) {
       return clientService.getClient(pass);
    }
}
