
import java.util.*;

class User {

    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Train {

    int trainNumber;
    String trainName;

    public Train(int trainNumber, String trainName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
    }
}

class Reservation {

    static int pnrCounter = 1000;
    int pnr;
    String name;
    int trainNumber;
    String trainName;
    String classType;
    String journeyDate;
    String from;
    String to;

    public Reservation(String name, int trainNumber, String trainName, String classType, String journeyDate, String from, String to) {
        this.pnr = ++pnrCounter;
        this.name = name;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.from = from;
        this.to = to;
    }

    public void printTicket() {
        System.out.println("----- Reservation Details -----");
        System.out.println("PNR: " + pnr);
        System.out.println("Name: " + name);
        System.out.println("Train No: " + trainNumber);
        System.out.println("Train Name: " + trainName);
        System.out.println("Class: " + classType);
        System.out.println("Date of Journey: " + journeyDate);
        System.out.println("From: " + from + " -> To: " + to);
        System.out.println("--------------------------------");
    }
}

public class OnlineReservationSystem {

    static Scanner sc = new Scanner(System.in);
    static Map<String, User> userDB = new HashMap<>();
    static Map<Integer, Train> trains = new HashMap<>();
    static Map<Integer, Reservation> reservations = new HashMap<>();

    public static void main(String[] args) {
        seedData();
        System.out.println("=== Welcome to Online Reservation System ===");
        System.out.print("Enter Username: ");
        String user = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        if (login(user, pass)) {
            System.out.println("Login Successful.\n");
            int choice;
            do {
                System.out.println("1. Reserve Ticket\n2. Cancel Ticket\n3. Exit");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        reserveTicket();
                        break;
                    case 2:
                        cancelTicket();
                        break;
                    case 3:
                        System.out.println("Thank you for using the system.");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } while (choice != 3);
        } else {
            System.out.println("Invalid credentials! Access denied.");
        }
    }

    static boolean login(String username, String password) {
        User user = userDB.get(username);
        return user != null && user.password.equals(password);
    }

    static void seedData() {
        // Adding dummy users
        userDB.put("admin", new User("admin", "1234"));
        userDB.put("user", new User("user", "pass"));

        // Adding dummy trains
        trains.put(101, new Train(101, "Rajdhani Express"));
        trains.put(102, new Train(102, "Shatabdi Express"));
        trains.put(103, new Train(103, "Duronto Express"));
    }

    static void reserveTicket() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter Train Number: ");
        int trainNumber = Integer.parseInt(sc.nextLine());

        Train train = trains.get(trainNumber);
        if (train == null) {
            System.out.println("Invalid Train Number.");
            return;
        }

        System.out.println("Train Name: " + train.trainName);
        System.out.print("Enter Class Type (Sleeper/AC): ");
        String classType = sc.nextLine();
        System.out.print("Enter Date of Journey (DD-MM-YYYY): ");
        String date = sc.nextLine();
        System.out.print("From: ");
        String from = sc.nextLine();
        System.out.print("To: ");
        String to = sc.nextLine();

        Reservation res = new Reservation(name, train.trainNumber, train.trainName, classType, date, from, to);
        reservations.put(res.pnr, res);

        System.out.println("Reservation Successful. Your PNR is: " + res.pnr);
        res.printTicket();
    }

    static void cancelTicket() {
        System.out.print("Enter your PNR Number: ");
        int pnr = Integer.parseInt(sc.nextLine());

        Reservation res = reservations.get(pnr);
        if (res == null) {
            System.out.println("Invalid PNR. No such reservation found.");
            return;
        }

        res.printTicket();
        System.out.print("Do you want to confirm cancellation? (yes/no): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            reservations.remove(pnr);
            System.out.println("Ticket cancelled successfully.");
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
}
