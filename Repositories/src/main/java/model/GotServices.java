package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gotservices")
public class GotServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name="service",referencedColumnName = "index")
    @OneToOne(fetch = FetchType.EAGER)
    private Service service;
    @JoinColumn(name="passofclient",referencedColumnName = "passnum")
    @OneToOne(fetch = FetchType.EAGER)
    private Client client;
    @Column(name = "date")
    private  Date date;
    @Column(name = "nameofservice")
    private String nameOfService;
    @Column(name = "price")
    private Integer price;
    public GotServices(){

    }
    public void setService(Service service) {
        this.service = service;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }


    public Service getService() {
        return service;
    }

    public Date getDate() {
        return date;
    }

    public String getNameOfService() {
        return nameOfService;
    }

    public void setNameOfService(String nameOfService) {
        this.nameOfService = nameOfService;
    }

    public int getPriceOfService(){
        return service.getPrice();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

