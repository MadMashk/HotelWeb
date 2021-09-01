package model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Table(name = "rent")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name="client",referencedColumnName = "passnum")
    @OneToOne(fetch = FetchType.EAGER)
    private Client client;
    @JoinColumn(name="room",referencedColumnName = "room_number")
    @OneToOne(fetch = FetchType.EAGER)
    private Room room;
    @Column(name = "arrivaldate")
    private Date arrivalDate;
    @Column(name = "departuredate")
    private Date departureDate;
    @Column(name = "price")
    private Integer price;
     public Rent(){

  }
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDates(String arrivalDate, int stayDuration) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Calendar cal = GregorianCalendar.getInstance();
        this.arrivalDate = simpleDateFormat.parse(arrivalDate);
        cal.setTime(this.arrivalDate);
        cal.add(GregorianCalendar.DAY_OF_MONTH, stayDuration);
        this.departureDate = cal.getTime();
    }

    public Room getRoom() {
        return room;
    }
    public int daysDifference(Date arrivalDate, Date departureDate){
        Long difference = departureDate.getTime()-arrivalDate.getTime();
        String a=String.format("%d", difference/86400000);
        return Integer.parseInt(a);
    }
    public int getPrice() {
        this.price=room.getPrice()*daysDifference(arrivalDate,departureDate);
        return price;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public String getClientName() {
        return client.getName();
    }

    public Client getClient() {
        return client;
    }
}