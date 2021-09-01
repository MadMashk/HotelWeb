package model;

import model.constants.RoomStatus;

import javax.persistence.*;
@Entity
@Table(name = "room")
public class Room  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_number")
    private Integer number;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "starsquantity")
    private Integer starsQuantity;
    @Column(name = "price")
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

    public int getNumber() {
        return number;
    }

    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStarsQuantity() {
        return starsQuantity;
    }

    public RoomStatus getStatus() {
        return status;
    }


}

