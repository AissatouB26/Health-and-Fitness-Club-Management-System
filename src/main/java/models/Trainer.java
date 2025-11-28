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
@Table(name = "trainers")
public class Trainer {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name, email;

    //Connecting with FitnessClass
    @OneToMany(mappedBy = "trainer")
    private List<FitnessClass> classes = new ArrayList<>();

    //Constructor
    public Trainer() {}

    public Trainer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
}