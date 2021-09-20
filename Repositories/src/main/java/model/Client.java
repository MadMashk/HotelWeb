package model;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "passnum", length = 9)
    private String passportNumber;
    @Column(name = "phonenumber", length = 11)
    private String phoneNumber;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="room_number")
    private Room room;
    public Client(){

    }
    public String getPassportNumber() {
        return passportNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
