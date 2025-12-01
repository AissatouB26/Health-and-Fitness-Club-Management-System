package models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
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
@Table(name = "fitnessclasses")

public class FitnessClass {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int capacity;

    @Column(name = "day_of_week", nullable=true)
    private int dayOfWeek;
    private LocalDate startTime, endTime;

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

    public FitnessClass(String name, int capacity, int dayOfWeek, LocalDate startTime, LocalDate endTime) {
        this.name = name;
        this.capacity = capacity;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public int getCapacity() {return capacity;}
    public int getDayOfWeek() {return dayOfWeek;}
    public LocalDate getStartTime() {return startTime;}
    public LocalDate getEndTime() {return endTime;}
    public Trainer getTrainer() {return trainer;}
    public Room getRoom() {return room;}

    public void setName(String name) {this.name = name;}
    public void setCapacity(int capacity) {this.capacity = capacity;}
    public void setDayOfWeek(int dayOfWeek) {this.dayOfWeek = dayOfWeek;}
    public void setStartTime(LocalDate startTime) {this.startTime = startTime;}
    public void setEndTime(LocalDate endTime) {this.endTime = endTime;}
    public void setTrainer(Trainer trainer) {this.trainer = trainer;}
    public void setRoom(Room room) {this.room = room;}

}