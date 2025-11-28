package models;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")

public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age, weight;
    private String email;

    //Connecting with FitnessClass
    @ManyToMany
    @JoinTable(
        name = "member_course",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<FitnessClass> classes = new ArrayList<>();

    //Constructor
    public Member() {}

    public Member(String name, int age, int weight, String email) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.email = email;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public int getAge(){ return age;}
    public String getEmail(){ return email; }
    public int getWeight(){return weight;}

    public void setName(String name) {this.name = name;}
    public void setAge(int age) {this.age = age;}
    public void setWeight(int weight) {this.weight = weight;}
    public void setEmail(String email) {this.email = email;}
}
