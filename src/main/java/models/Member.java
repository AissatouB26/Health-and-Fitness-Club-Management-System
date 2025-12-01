package models;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "member")

public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age, weight, height;
    private Integer steps;

    @Column(name ="heart_rate")
    private Integer heartRate;

    @Column(name="goal_weight")
    private Integer goalWeight;

    private String email;

    //Connecting with FitnessClass
    @ManyToMany (fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
        name = "member_course",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<FitnessClass> classes = new ArrayList<>();

    //Constructor
    public Member() {}

    public Member(String name, int age, int weight, int height, String email) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.email = email;
    }

    //Getters and Setters
    public int getId() {return id;}
    public String getName() {return name;}
    public int getAge(){ return age;}
    public String getEmail(){ return email; }
    public int getWeight(){return weight;}
    public int getHeight(){return height;}
    public Integer getGoalWeight(){return goalWeight;}
    public Integer getHeartRate(){return heartRate;}
    public Integer getSteps(){return steps;}
    public List<FitnessClass> getClasses(){return classes;}

    public void setName(String name) {this.name = name;}
    public void setAge(int age) {this.age = age;}
    public void setWeight(int weight) {this.weight = weight;}
    public void setEmail(String email) {this.email = email;}
    public void setHeight(int height) {this.height = height;}
    public void setGoalWeight(int goalWeight) {this.goalWeight = goalWeight;}
    public void setHeartRate(int heartRate) {this.heartRate = heartRate;}
    public void setSteps(int steps) {this.steps = steps;}
    public void setClasses(List<FitnessClass> classes) {this.classes = classes;}

    //Check availability in a class
    public boolean isAvailable(FitnessClass newClass) {
        for (FitnessClass existingClass : classes) {
            // Check for time overlap
            if(existingClass.getDayOfWeek() == newClass.getDayOfWeek()) {
                if (!newClass.getEndTime().isBefore(existingClass.getStartTime()) &&
                    !newClass.getStartTime().isAfter(existingClass.getEndTime())) {
                    return false; 
                }
            }
        }
        return true; 
    }
    
}
