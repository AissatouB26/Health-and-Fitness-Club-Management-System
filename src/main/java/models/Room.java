package models;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int roomNumber;
    private int capacity;

    //Connecting with Admin
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    //Connecting with Equipment
    @OneToMany(mappedBy = "room")
    private List<Equipment> equipments = new ArrayList<>();

    //Connecting with FitnessClass
    @OneToMany(mappedBy = "room")
    private List<FitnessClass> classes = new ArrayList<>();

    //Constructor
    public Room() {}

    public Room(int roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    //Getters and Setters
    public int getId() {return id;}
    public int getRoomNumber() {return roomNumber;}
    public int getCapacity() {return capacity;}

    public void setName(int roomNumber) {this.roomNumber = roomNumber;}
    public void setCapacity(int capacity) {this.capacity = capacity;}
}
