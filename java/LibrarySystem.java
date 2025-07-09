
import java.util.*;

class Book {

    String id, title, author, category;
    boolean isIssued;

    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = false;
    }

    public String toString() {
        return "[" + id + "] " + title + " by " + author + " | Category: " + category + (isIssued ? " | ❌ Issued" : " | ✅ Available");
    }
}

class User {

    String username, email;
    List<Book> issuedBooks = new ArrayList<>();

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void viewIssuedBooks() {
        if (issuedBooks.isEmpty()) {
            System.out.println("No books issued.");
        } else {
            System.out.println("📚 Books currently issued:");
            for (Book b : issuedBooks) {
                System.out.println(b);
            }
        }
    }
}

class Admin {

    String username = "admin";
    String password = "admin123";
}

class Library {

    Map<String, Book> books = new HashMap<>();
    Map<String, User> users = new HashMap<>();
    Scanner sc = new Scanner(System.in);

    public Library() {
        // Sample data
        books.put("B001", new Book("B001", "Java Basics", "James Gosling", "Programming"));
        books.put("B002", new Book("B002", "The Alchemist", "Paulo Coelho", "Fiction"));
        users.put("lalith", new User("lalith", "lalith@example.com"));
    }

    // Admin operations
    void adminMenu() {
        int choice;
        do {
            System.out.println("\n===== Admin Panel =====");
            System.out.println("1. Add Book");
            System.out.println("2. Delete Book");
            System.out.println("3. View All Books");
            System.out.println("4. View All Users");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    listBooks();
                    break;
                case 4:
                    listUsers();
                    break;
                case 5:
                    System.out.println("Logged out from admin panel.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    void addBook() {
        System.out.print("Enter Book ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        books.put(id, new Book(id, title, author, category));
        System.out.println("✅ Book added.");
    }

    void deleteBook() {
        System.out.print("Enter Book ID to delete: ");
        String id = sc.nextLine();
        if (books.containsKey(id)) {
            books.remove(id);
            System.out.println("✅ Book removed.");
        } else {
            System.out.println("❌ Book not found.");
        }
    }

    void listBooks() {
        System.out.println("\n📚 Library Books:");
        for (Book b : books.values()) {
            System.out.println(b);
        }
    }

    void listUsers() {
        System.out.println("\n👥 Registered Users:");
        for (User u : users.values()) {
            System.out.println("Username: " + u.username + ", Email: " + u.email);
        }
    }

    // User operations
    void userMenu(User user) {
        int choice;
        do {
            System.out.println("\n===== User Dashboard =====");
            System.out.println("1. View All Books");
            System.out.println("2. Search Book");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. View My Issued Books");
            System.out.println("6. Send Query");
            System.out.println("7. Logout");
            System.out.print("Choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    searchBook();
                    break;
                case 3:
                    issueBook(user);
                    break;
                case 4:
                    returnBook(user);
                    break;
                case 5:
                    user.viewIssuedBooks();
                    break;
                case 6:
                    EmailService.sendEmail(user);
                    break;
                case 7:
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 7);
    }

    void searchBook() {
        System.out.print("Enter title keyword: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(keyword)) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching books found.");
        }
    }

    void issueBook(User user) {
        System.out.print("Enter Book ID to issue: ");
        String id = sc.nextLine();
        Book book = books.get(id);
        if (book != null && !book.isIssued) {
            book.isIssued = true;
            user.issuedBooks.add(book);
            System.out.println("✅ Book issued.");
        } else {
            System.out.println("❌ Book not available.");
        }
    }

    void returnBook(User user) {
        System.out.print("Enter Book ID to return: ");
        String id = sc.nextLine();
        Book book = books.get(id);
        if (book != null && user.issuedBooks.contains(book)) {
            book.isIssued = false;
            user.issuedBooks.remove(book);
            System.out.println("✅ Book returned.");
        } else {
            System.out.println("❌ Invalid return.");
        }
    }
}

class EmailService {

    public static void sendEmail(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your query: ");
        String msg = sc.nextLine();
        System.out.println("📧 Email sent to admin from " + user.email + ": " + msg);
    }
}

public class LibrarySystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library lib = new Library();
        Admin admin = new Admin();

        System.out.println("📚 Welcome to the Digital Library Management System");
        System.out.print("Login as (admin/user): ");
        String role = sc.nextLine();

        if (role.equalsIgnoreCase("admin")) {
            System.out.print("Enter admin username: ");
            String uname = sc.nextLine();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();
            if (uname.equals(admin.username) && pass.equals(admin.password)) {
                lib.adminMenu();
            } else {
                System.out.println("❌ Invalid admin credentials.");
            }
        } else if (role.equalsIgnoreCase("user")) {
            System.out.print("Enter your username: ");
            String uname = sc.nextLine();
            User u = lib.users.get(uname);
            if (u != null) {
                lib.userMenu(u);
            } else {
                System.out.println("❌ User not found.");
            }
        } else {
            System.out.println("❗ Invalid role.");
        }
    }
}
