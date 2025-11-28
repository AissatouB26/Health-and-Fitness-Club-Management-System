package models;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "classes")

public class FitnessClass {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int capacity;
    private LocalDateTime startTime, endTime;

    //Connecting with Member
    @ManyToMany
    @JoinTable(
        name = "member_course",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members = new ArrayList<>();

    //Connecting with Trainer
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    //Connecting with Admin
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    //Connecting with Room
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    //Constructor
    public FitnessClass() {}

    public FitnessClass(String name, int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public int getCapacity() {return capacity;}
    public LocalDateTime getStartTime() {return startTime;}
    public LocalDateTime getEndTime() {return endTime;}

    public void setName(String name) {this.name = name;}
    public void setCapacity(int capacity) {this.capacity = capacity;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

}