package web.controllers;
import model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.ServiceService;

import java.util.List;

@RestController
@RequestMapping(value = ("services"))
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json") //список услуг
    public List<Service> addService  (){
        return serviceService.getAllServices();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json") //добавить услугу
    public Service addService  (@RequestBody Service service){
        return serviceService.saveOrAddService(service);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=application/json") //обновить услугу
    public Service updateService  (@RequestBody Service service){
        return serviceService.saveOrAddService(service);
    }
}
