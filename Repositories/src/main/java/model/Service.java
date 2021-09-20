package model;

import javax.persistence.*;
@Entity
@Table(name = "service")
public class Service  {
    @Column(name = "name")
    private  String name;
    @Column(name = "price")
    private  Integer price;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "index")
    private  Integer index;
    public Service(){

    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
