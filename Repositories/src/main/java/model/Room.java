package model;

import model.constants.RoomStatus;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
@Entity
@Table(name = "room")
public class Room  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_number")
    private Integer number;
    @Column(name = "capacity")
    @Length(min = 1, max = 9)
    private Integer capacity;
    @Column(name = "starsquantity")
    @Length(min = 1, max = 3)
    private Integer starsQuantity;
    @Column(name = "price")
    @Length(min = 1000)
    private Integer price;
    @Column(name = "status")
    private RoomStatus status;
    @Column(name = "rentstatus")
    private boolean rentStatus;

   public Room(){
        setStatus(RoomStatus.FREE);
        setRentStatus(false);
    }
    public void setCapacity(int capacity){
        this.capacity=capacity;
    }

    public boolean getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setStarsQuantity(int starsQuantity) {
        this.starsQuantity = starsQuantity;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getStarsQuantity() {
        return starsQuantity;
    }

    public RoomStatus getStatus() {
        return status;
    }


}

