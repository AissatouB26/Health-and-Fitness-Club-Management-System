package models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipments")
public class Equipment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String type;
    private String status;

    //Connecting with Room
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    //Connecting with Admin
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    //Constructor
    public Equipment() {}

    public Equipment(String name, String type, String status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getStatus() {return status;}
    public Room getRoom() {return room;}

    public void setName(String name) {this.name = name;}
    public void setType(String type) {this.type = type;}
    public void setStatus(String status) {this.status = status;}
    public void setRoom(Room room) {this.room = room;}
}
