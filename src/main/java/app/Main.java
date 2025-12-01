package app;
import java.io.File;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.Equipment;
import models.FitnessClass;
import models.Member;
import models.Room;
import models.Trainer;


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
            List<Equipment> equipment = session.createQuery("from Equipment", Equipment.class).list();
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
             session.getTransaction().commit();

             //App main loop
            while (true) {
                session.beginTransaction();
                /* Member Interactions */
                if(user instanceof Member){
                    System.out.println("What would you like to do?");
                    System.out.println("1. Update Profile");
                    System.out.println("4. Exit");
                    String action = scanner.nextLine();
                    switch(action) {
                        case "1":
                            updateProfile((Member) user);
                            session.persist(user);
                            break;
                        case "4":
                            return;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                /* Admin Interactions */
                else if(user instanceof Admin){
                    System.out.println("What would you like to do?");
                    System.out.println("1. Manage Classes");
                    System.out.println("2. Book Rooms");
                    System.out.println("3. Manage Equipment");
                    System.out.println("4. Exit");
                    String action = scanner.nextLine();
                    switch(action) {
                        case "1":
                            System.out.println("What would you like to do?");
                            System.out.println("1. Create Fitness Class");
                            System.out.println("2. Assign Trainer to Class");
                            System.out.println("3. Update Class Schedule");
                            System.out.println("4. View Classes");
                            String classAction = scanner.nextLine();
                            switch(classAction) {
                                case "1":
                                    createFitnessClass(session, classes);
                                    break;
                                case "2":
                                    assignTrainerToClass(session, trainers, classes);
                                    break;
                                case "3":
                                    updateClassSchedule(session, classes);
                                    break;
                                case "4":
                                    viewAllClasses(classes);
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                        case "2":
                            System.out.println("What would you like to do?");
                            System.out.println("1. Assign Room to Class");
                            System.out.println("2. Unassign  Room from Class");
                            System.out.println("3. View All Rooms");
                            String roomAction = scanner.nextLine();
                            switch(roomAction) {
                                case "1":
                                    assignRoomToClasses(session, rooms, classes);
                                    break;
                                case "2":
                                    unassignRoomFromClass(session, classes);
                                    break;
                                case "3":
                                    viewAllRooms(rooms);
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                        case "3":
                            System.out.println("What would you like to do?");
                            System.out.println("1. Update Equipment Status");
                            System.out.println("2. Assign Equipment to Room");
                            System.out.println("3. View All Equipment");
                            String equipAction = scanner.nextLine();
                            switch(equipAction) {
                                case "1":
                                    updateEquipmentStatus(session, equipment);
                                    break;
                                case "2":
                                    assignEquipmentToRoom(session, equipment, rooms);
                                    break;
                                case "3":
                                    viewAllEquipment(equipment);
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                        case "4":
                            return;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                session.getTransaction().commit();
            }
    
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

    //Function to create classes 
    private static void createFitnessClass(Session session, List<FitnessClass> classes) {
        System.out.print("Enter class name: ");
        String name = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter day of week (1=Sunday, 7=Saturday): ");
        int dayOfWeek = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter start time (Hours:Minutes): ");
        String startInput = scanner.nextLine();
        System.out.print("Enter end time (Hours:Minutes): ");
        String endInput = scanner.nextLine();

        FitnessClass newClass = new FitnessClass(name, capacity, dayOfWeek, 
            LocalTime.parse(startInput), LocalTime.parse(endInput));
        session.persist(newClass);
        classes.add(newClass);
        System.out.println("Created new class: " + name);
    }

    //Function to assign trainers to classes
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
            System.out.println((i + 1) + ". Room " + rooms.get(i).getRoomNumber() + " (Capacity: " + rooms.get(i).getCapacity() + ")");
        }
        System.out.print("Select a room by number: ");
        int roomIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Room selectedRoom = rooms.get(roomIndex);

        System.out.println("Available Classes:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName() + " (Capacity: " + classes.get(i).getCapacity() + ")");
        }
        System.out.print("Select a class by number: ");
        int classIndex = Integer.parseInt(scanner.nextLine()) - 1;
        FitnessClass selectedClass = classes.get(classIndex);

        if(selectedRoom.isAvailable(selectedClass) == false) {
            System.out.println("Room is not available for this class time.");
            return;
        }
        else if(selectedRoom.getCapacity() < selectedClass.getCapacity()) {
            System.out.println("Room capacity is less than class capacity.");
            return;
        }

        selectedClass.setRoom(selectedRoom);
        session.merge(selectedClass);
        System.out.println("Assigned Room " + selectedRoom.getRoomNumber() + " to " + selectedClass.getName());
    }
    
    //Function to manage update equipment status
    private static void updateEquipmentStatus(Session session, List<Equipment> equipment) {
        System.out.println("All Equipment:");
        for (int i = 0; i < equipment.size(); i++) {
            Equipment e = equipment.get(i);
            String roomInfo = (e.getRoom() != null) ? " - Room " + e.getRoom().getRoomNumber() : " - No Room Assigned";
            System.out.println((i + 1) + ". " + e.getType() + " - " + e.getStatus() + roomInfo);
        }
        
        System.out.print("Select equipment by number: ");
        int equipmentIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Equipment selectedEquipment = equipment.get(equipmentIndex);

        System.out.print("Enter new status for " + selectedEquipment.getType() + ": ");
        String newStatus = scanner.nextLine();
        selectedEquipment.setStatus(newStatus);
        session.persist(selectedEquipment);
        System.out.println("Updated status of " + selectedEquipment.getType() + " to " + newStatus);

    }

    //Function that assigns equipment to rooms
    private static void assignEquipmentToRoom(Session session, List<Equipment> equipment, List<Room> rooms) {
        System.out.println("Available Equipment:");
        for (int i = 0; i < equipment.size(); i++) {
            Equipment e = equipment.get(i);
            String roomInfo = (e.getRoom() != null) ? " - Room " + e.getRoom().getRoomNumber() : " - No Room Assigned";
            System.out.println((i + 1) + ". " + e.getType() + " - " + e.getStatus() + roomInfo);
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
        System.out.println("Assigned " + selectedEquipment.getType() + " to Room " + selectedRoom.getRoomNumber());
    }

    //Update class scheduele
    private static void updateClassSchedule(Session session, List<FitnessClass> classes) {
        System.out.println("Available Classes:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName());
        }
        System.out.print("Select a class by number: ");
        int classIndex = Integer.parseInt(scanner.nextLine()) - 1;
        FitnessClass selectedClass = classes.get(classIndex);
        int newDay;
        LocalTime newStart, newEnd;

        System.out.print("Enter new day of week (1=Sunday, 7=Saturday) (current: " + selectedClass.getDayOfWeek() + "): ");
        String dayInput = scanner.nextLine();
        if (!dayInput.isBlank()) {
            newDay = Integer.parseInt(dayInput);
        } else {
            newDay = selectedClass.getDayOfWeek();
        }

        System.out.print("Enter new start time (YYYY-MM-DD) (current: " + selectedClass.getStartTime() + "): ");
        String startInput = scanner.nextLine();
        if (!startInput.isBlank()) {
            newStart = LocalTime.parse(startInput);
        } else {
            newStart = selectedClass.getStartTime();
        }

        System.out.print("Enter new end time (YYYY-MM-DD) (current: " + selectedClass.getEndTime() + "): ");
        String endInput = scanner.nextLine();
        if (!endInput.isBlank()) {
            newEnd = LocalTime.parse(endInput);
        } else {
            newEnd = selectedClass.getEndTime();
        }

        // Temporarily create a copy with new schedule for availability check
        FitnessClass temp = new FitnessClass();
        temp.setRoom(selectedClass.getRoom());
        temp.setDayOfWeek(newDay);
        temp.setStartTime(newStart);
        temp.setEndTime(newEnd);

        //Check if assigned room is available
        Room assignedRoom = temp.getRoom();
        if (assignedRoom != null && !assignedRoom.isAvailable(temp)) {
            System.out.println("Warning: The assigned room is not available for the new schedule.");
            System.out.print("Please assign a different room  or adjust the schedule");
            return;
        }

        //If all good, update class schedule
        selectedClass.setDayOfWeek(newDay);
        selectedClass.setStartTime(newStart);
        selectedClass.setEndTime(newEnd);
        session.merge(selectedClass);
        System.out.println("Updated schedule for " + selectedClass.getName());
    }

    //Function to view all classes
    private static void viewAllClasses(List<FitnessClass> classes) {
        System.out.println("All Fitness Classes:");
        for (FitnessClass fitnessClass : classes) {
            System.out.println("Class Name: " + fitnessClass.getName() +
                               ", Capacity: " + fitnessClass.getCapacity() +
                               ", Day of Week: " + fitnessClass.getDayOfWeek() +
                               ", Start Time: " + fitnessClass.getStartTime() +
                               ", End Time: " + fitnessClass.getEndTime() +
                               ", Trainer: " + (fitnessClass.getTrainer() != null ? fitnessClass.getTrainer().getName() : "None") +
                               ", Room: " + (fitnessClass.getRoom() != null ? fitnessClass.getRoom().getRoomNumber() : "None"));
        }
    }

    //Function to view all equipment
    private static void viewAllEquipment(List<Equipment> equipment) {
        System.out.println("All Equipment:");
        for (Equipment e : equipment) {
            String roomInfo = (e.getRoom() != null) ? " - Room " + e.getRoom().getRoomNumber() : " - No Room Assigned";
            System.out.println("Type: " + e.getType() + ", Status: " + e.getStatus() + roomInfo);
        }
    }   

    //Function to view all rooms
    private static void viewAllRooms(List<Room> rooms) {
        System.out.println("All Rooms:");
        for (Room r : rooms) {
            System.out.println("Room Number: " + r.getRoomNumber() + ", Capacity: " + r.getCapacity());
        }
    }

    //Unassign a room from a class
    private static void unassignRoomFromClass(Session session, List<FitnessClass> classes) {
        System.out.println("Available Classes:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName());
        }
        System.out.print("Select a class by number to unassign its room: ");
        int classIndex = Integer.parseInt(scanner.nextLine()) - 1;
        FitnessClass selectedClass = classes.get(classIndex);

        selectedClass.setRoom(null);
        session.merge(selectedClass);
        System.out.println("Unassigned room from " + selectedClass.getName());
    }
}


