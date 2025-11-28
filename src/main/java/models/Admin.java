package models;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name, email;

    //Connecting with Room
    @OneToMany(mappedBy = "admin")
    private List<Room> rooms = new ArrayList<>();

    //Connecting with Equipment
    @OneToMany(mappedBy = "admin")
    private List<Equipment> equipments = new ArrayList<>();

    //Conecting with FitnessClass
    @OneToMany(mappedBy = "admin")
    private List<FitnessClass> classes = new ArrayList<>();

    //Constructor
    public Admin() {}
}
