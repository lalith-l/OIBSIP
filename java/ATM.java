
import java.util.*;

class Transaction {

    String type;
    double amount;
    Date date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
    }

    public String toString() {
        return date + " - " + type + ": ₹" + amount;
    }
}

class User {

    String userId;
    String pin;
    double balance;
    List<Transaction> transactions = new ArrayList<>();

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
    }

    public void addTransaction(String type, double amount) {
        transactions.add(new Transaction(type, amount));
    }

    public void showHistory() {
        if (transactions.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            System.out.println("🧾 Transaction History:");
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }
}

class BankDatabase {

    Map<String, User> users = new HashMap<>();

    public BankDatabase() {
        // Adding some users
        users.put("user1", new User("user1", "1234", 10000.0));
        users.put("lalith", new User("lalith", "1234", 20000.0));
    }

    public User validateUser(String userId, String pin) {
        User u = users.get(userId);
        if (u != null && u.pin.equals(pin)) {
            return u;
        }
        return null;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}

class ATMOperations {

    Scanner sc = new Scanner(System.in);
    BankDatabase db;

    public ATMOperations(BankDatabase db) {
        this.db = db;
    }

    public void showMenu(User user) {
        int choice;
        do {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    user.showHistory();
                    break;
                case 2:
                    withdraw(user);
                    break;
                case 3:
                    deposit(user);
                    break;
                case 4:
                    transfer(user);
                    break;
                case 5:
                    System.out.println("👋 Thank you for using the ATM.");
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        } while (choice != 5);
    }

    void withdraw(User user) {
        System.out.print("Enter amount to withdraw: ₹");
        double amount = Double.parseDouble(sc.nextLine());
        if (amount > user.balance) {
            System.out.println("❌ Insufficient balance!");
        } else {
            user.balance -= amount;
            user.addTransaction("Withdraw", amount);
            System.out.println("✅ ₹" + amount + " withdrawn. New Balance: ₹" + user.balance);
        }
    }

    void deposit(User user) {
        System.out.print("Enter amount to deposit: ₹");
        double amount = Double.parseDouble(sc.nextLine());
        user.balance += amount;
        user.addTransaction("Deposit", amount);
        System.out.println("✅ ₹" + amount + " deposited. New Balance: ₹" + user.balance);
    }

    void transfer(User user) {
        System.out.print("Enter recipient user ID: ");
        String recipientId = sc.nextLine();
        User recipient = db.getUser(recipientId);

        if (recipient == null) {
            System.out.println("❌ Recipient not found.");
            return;
        }

        System.out.print("Enter amount to transfer: ₹");
        double amount = Double.parseDouble(sc.nextLine());

        if (amount > user.balance) {
            System.out.println("❌ Insufficient balance.");
        } else {
            user.balance -= amount;
            recipient.balance += amount;
            user.addTransaction("Transfer to " + recipientId, amount);
            recipient.addTransaction("Received from " + user.userId, amount);
            System.out.println("✅ ₹" + amount + " transferred to " + recipientId);
        }
    }
}

public class ATM {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankDatabase db = new BankDatabase();
        ATMOperations operations = new ATMOperations(db);

        System.out.println("💳 Welcome to the Java ATM!");
        System.out.print("Enter User ID: ");
        String userId = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        User user = db.validateUser(userId, pin);
        if (user != null) {
            System.out.println("✅ Login successful. Hello, " + userId + "!");
            operations.showMenu(user);
        } else {
            System.out.println("❌ Invalid credentials.");
        }
    }
}
