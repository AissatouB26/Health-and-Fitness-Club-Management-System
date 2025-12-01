package app;
import java.io.File;
import java.util.List;
import java.util. Scanner;
import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.Equipment;
import models.FitnessClass;
import models.Member;
import models.Trainer;
import models.Room;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // Point to hibernate.cfg.xml in project root
        Configuration cfg = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml"));
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Object user;

            //Load data from DB
            List<Member> members = session.createQuery("from Member", Member.class).list();
            List<Trainer> trainers = session.createQuery("from Trainer", Trainer.class).list();
            List<Admin> admins = session.createQuery("from Admin", Admin.class).list();
            List<Equipment> equipments = session.createQuery("from Equipment", Equipment.class).list();
            List<FitnessClass> classes = session.createQuery("from FitnessClass", FitnessClass.class).list();
            List<Room> rooms = session.createQuery("from Room", Room.class).list();

            // Login or Signup Function
            System.out.println("Welcome to the Fitness Center Management System");
            System.out.println("Type 1 to login or 2 to signup:");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {  
                user = login(members, trainers, admins);
                if (user == null) {
                    System.out.println("Login failed. Exiting.");
                    return;
                }
            } else if (choice.equals("2")) {
                user = signup(members);
                if (user == null) {
                    System.out.println("Signup failed. Exiting.");
                    return;
                }
                session.persist(user);
                members.add((Member)user);
            } else {
                System.out.println("Invalid choice. Exiting.");
                return;
            }

            /* Member Interactions */
            if(user instanceof Member){
                System.out.println("What would you like to do?");
                System.out.println("1. Update Profile");
                String action = scanner.nextLine();
                if (action.equals("1")) {
                    updateProfile((Member) user);
                    session.persist(user);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }

    //Login and Signup Methods
    private static Object login(List<Member> members, List<Trainer> trainers, List<Admin> admins) {
        // Implement login logic here
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Check in Members
        for (Member member : members) {
            if (member.getEmail().equals(email)) {
                System.out.println("Logged in as Member: " + member.getName());
                return member;
            }
        }

        // Check in Trainers
        for (Trainer trainer : trainers) {
            if (trainer.getEmail().equals(email)) {
                System.out.println("Logged in as Trainer: " + trainer.getName());
                return trainer;
            }
        }

        // Check in Admins
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email)) {
                System.out.println("Logged in as Admin: " + admin.getName());
                return admin;
            }
        }

        System.out.println("No user found with the provided email.");
        return null;
    }

    private static Member signup(List<Member> members) {
        // Implement signup logic here
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter weight: ");
        int weight = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter height: ");
        int height = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Check if email already exists
        for (Member member : members) {
            if (member.getEmail().equals(email)) {
                System.out.println("Email already registered. Signup failed.");
                return null;
            }
        }

        Member newMember = new Member(name, age, weight, height, email);
        System.out.println("Signup successful. Welcome, " + name + "!");
        return newMember;
    }

    /* Member functions */
    //Function that allows members to update their profile
    private static void updateProfile(Member member) {
        System.out.println("Note: Press enter to skip updating a field.");

        System.out.print("Enter new name (current: " + member.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) {
            member.setName(name);
        }

        System.out.print("Enter new age (current: " + member.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isBlank()) {
            member.setAge(Integer.parseInt(ageInput));
        }

        System.out.print("Enter new weight (current: " + member.getWeight() + "): ");
        String weightInput = scanner.nextLine();
        if (!weightInput.isBlank()) {
            member.setWeight(Integer.parseInt(weightInput));
        }

        System.out.print("Enter new height (current: " + member.getHeight() + "): ");
        String heightInput = scanner.nextLine();
        if (!heightInput.isBlank()) {
            member.setHeight(Integer.parseInt(heightInput));
        }

        System.out.print("Enter new email (current: " + member.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) {
            member.setEmail(email);
        }

        System.out.print("Enter new goal weight (current: " + member.getGoalWeight() + "): ");
        String goalWeightInput = scanner.nextLine();
        if (!goalWeightInput.isBlank()) {
            member.setGoalWeight(Integer.parseInt(goalWeightInput));
        }

        System.out.print("Enter heart rate (current: " + member.getHeartRate() + "): ");
        String heartRateInput = scanner.nextLine();
        if (!heartRateInput.isBlank()) {
            member.setHeartRate(Integer.parseInt(heartRateInput));
        }

        System.out.print("Enter step count (current: " + member.getSteps() + "): ");
        String stepsInput = scanner.nextLine();
        if (!stepsInput.isBlank()) {
            member.setSteps(Integer.parseInt(stepsInput));
        }

        System.out.println("Profile updated!");
    } 


    //Function that displays latest health stats, active goals, past class count and upcoming sessions


    //Function that allows members to book a class


    /*Trainer functions*/
    //Function that allows trainers to view their schedule and assigned classes

    
    //Function that allows trainers to search for members by name and view their current goal and last metric


    /*Admin functions*/

    //Function that allows admins to create classes 
    private static void createFitnessClass(Session session) {
        System.out.print("Enter class name: ");
        String name = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter day of week (1=Sunday, 7=Saturday): ");
        int dayOfWeek = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter start time (YYYY-MM-DD): ");
        String startInput = scanner.nextLine();
        System.out.print("Enter end time (YYYY-MM-DD): ");
        String endInput = scanner.nextLine();

        FitnessClass newClass = new FitnessClass(name, capacity, dayOfWeek, 
            LocalDate.parse(startInput), LocalDate.parse(endInput));
        session.persist(newClass);
        System.out.println("Created new class: " + name);
    }

    //Function that allows admins to assign trainers to classes
    private static void assignTrainerToClass(Session session, List<Trainer> trainers, List<FitnessClass> classes) {
        System.out.println("Available Trainers:");
        for (int i = 0; i < trainers.size(); i++) {
            System.out.println((i + 1) + ". " + trainers.get(i).getName());
        }
        System.out.print("Select a trainer by number: ");
        int trainerIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Trainer selectedTrainer = trainers.get(trainerIndex);

        System.out.println("Available Classes:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName());
        }
        System.out.print("Select a class by number: ");
        int classIndex = Integer.parseInt(scanner.nextLine()) - 1;
        FitnessClass selectedClass = classes.get(classIndex);

        if(selectedTrainer.isAvailable(selectedClass) == false) {
            System.out.println("Trainer is not available for this class time.");
            return;
        }

        selectedClass.setTrainer(selectedTrainer);
        session.persist(selectedClass);
        System.out.println("Assigned " + selectedTrainer.getName() + " to " + selectedClass.getName());
    }

    //Function to assign rooms to class
    private static void assignRoomToClasses(Session session, List<Room> rooms, List<FitnessClass> classes) {
        System.out.println("Available Rooms:");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". Room " + rooms.get(i).getRoomNumber());
        }
        System.out.print("Select a room by number: ");
        int roomIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Room selectedRoom = rooms.get(roomIndex);

        System.out.println("Available Classes:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName());
        }
        System.out.print("Select a class by number: ");
        int classIndex = Integer.parseInt(scanner.nextLine()) - 1;
        FitnessClass selectedClass = classes.get(classIndex);

        if(selectedRoom.isAvailable(selectedClass) == false) {
            System.out.println("Room is not available for this class time.");
            return;
        }

        selectedClass.setRoom(selectedRoom);
        session.update(selectedClass);
        System.out.println("Assigned Room " + selectedRoom.getRoomNumber() + " to " + selectedClass.getName());
    }
    
    //Function that allows admins to manage equipment inventory
    private static void updateEquipmentStatus(Session session, List<Equipment> equipment) {
        System.out.println("All Equipment:");
        for (int i = 0; i < equipment.size(); i++) {
            System.out.println((i + 1) + ". " + equipment.get(i).getName() + " - " + equipment.get(i).getStatus() + equipment.get(i).getRoom().getRoomNumber());
        }
        
        System.out.print("Select equipment by number: ");
        int equipmentIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Equipment selectedEquipment = equipment.get(equipmentIndex);

        System.out.print("Enter new status for " + selectedEquipment.getName() + ": ");
        String newStatus = scanner.nextLine();
        selectedEquipment.setStatus(newStatus);
        session.persist(selectedEquipment);
        System.out.println("Updated status of " + selectedEquipment.getName() + " to " + newStatus);

    }

    //Function that assigns equipment to rooms
    private static void assignEquipmentToRoom(Session session, List<Equipment> equipment, List<Room> rooms) {
        System.out.println("Available Equipment:");
        for (int i = 0; i < equipment.size(); i++) {
            System.out.println((i + 1) + ". " + equipment.get(i).getName() + " - " + equipment.get(i).getStatus() + equipment.get(i).getRoom().getRoomNumber());
        }
        System.out.print("Select equipment by number: ");
        int equipmentIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Equipment selectedEquipment = equipment.get(equipmentIndex);

        System.out.println("Available Rooms:");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". Room " + rooms.get(i).getRoomNumber());
        }
        System.out.print("Select a room by number: ");
        int roomIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Room selectedRoom = rooms.get(roomIndex);

        selectedEquipment.setRoom(selectedRoom);
        session.persist(selectedEquipment);
        System.out.println("Assigned " + selectedEquipment.getName() + " to Room " + selectedRoom.getRoomNumber());
    }


    //Function that allows admins to manage rooms



}


