package web.controllers;
import model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ServiceService;

import java.util.List;

@RestController
@RequestMapping(value = ("services"))
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json") //список услуг
    public List<Service> addService  (@RequestParam Integer sortIndex){
        return serviceService.getAllServices(sortIndex);
    }
    @RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json") //добавить услугу
    public Service addService  (@RequestBody Service service){
        return serviceService.addService(service);
    }
    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=application/json") //TODO change обновить услугу
    public Service updateService  (@RequestParam("index") int index, @RequestBody Service service){
        return serviceService.update(index,service);
    }
    @RequestMapping(value = "/", method = RequestMethod.DELETE) //удалить услугу
    public void delete (@RequestParam("index") int index){
        serviceService.deleteService(index);
    }


}
