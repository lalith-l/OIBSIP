
import java.util.*;

class User {

    String username;
    String password;
    String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public void updateProfile(String name, String password) {
        this.fullName = name;
        this.password = password;
    }
}

class UserDatabase {

    Map<String, User> users = new HashMap<>();

    public UserDatabase() {
        users.put("student", new User("student", "1234", "Student A"));
    }

    public User validate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }

    public User getUser(String username) {
        return users.get(username);
    }
}

class Question {

    String question;
    String[] options;
    char correctAnswer;

    public Question(String question, String[] options, char correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void display(int number) {
        System.out.println("\nQ" + number + ": " + question);
        System.out.println("A. " + options[0]);
        System.out.println("B. " + options[1]);
        System.out.println("C. " + options[2]);
        System.out.println("D. " + options[3]);
    }

    public boolean checkAnswer(char answer) {
        return Character.toUpperCase(answer) == correctAnswer;
    }
}

class Exam {

    List<Question> questions = new ArrayList<>();
    Map<Integer, Character> userAnswers = new HashMap<>();
    Scanner sc = new Scanner(System.in);
    int score = 0;
    boolean submitted = false;

    public Exam() {
        questions.add(new Question("Which keyword is used to inherit a class in Java?", new String[]{"this", "super", "extends", "implements"}, 'C'));
        questions.add(new Question("Java is a:", new String[]{"Compiled Language", "Interpreted Language", "Both", "None"}, 'C'));
        questions.add(new Question("Which is not a primitive type in Java?", new String[]{"int", "float", "String", "double"}, 'C'));
    }

    public void start() {
        Timer timer = new Timer();
        System.out.println("⏳ Exam started! You have 60 seconds.");
        timer.schedule(new TimerTask() {
            public void run() {
                submitted = true;
                System.out.println("\n⏰ Time is up! Submitting your exam automatically.");
            }
        }, 60000); // 60 seconds

        for (int i = 0; i < questions.size() && !submitted; i++) {
            questions.get(i).display(i + 1);
            System.out.print("Your answer (A/B/C/D): ");
            String ans = sc.nextLine().toUpperCase();
            if (ans.length() == 1 && "ABCD".contains(ans)) {
                userAnswers.put(i, ans.charAt(0));
            } else {
                System.out.println("Invalid answer. Skipped.");
            }
        }

        submitted = true;
        timer.cancel();
        evaluate();
    }

    public void evaluate() {
        System.out.println("\n📝 Exam Submitted. Evaluating...");
        for (int i = 0; i < questions.size(); i++) {
            char userAns = userAnswers.getOrDefault(i, 'X');
            if (questions.get(i).checkAnswer(userAns)) {
                score += 1;
            }
        }
        System.out.println("✅ You scored: " + score + "/" + questions.size());
    }
}

class ExamSystem {

    Scanner sc = new Scanner(System.in);
    UserDatabase db = new UserDatabase();

    public void run() {
        System.out.println("📚 Welcome to the Online Examination System");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = db.validate(username, password);
        if (user == null) {
            System.out.println("❌ Invalid credentials. Access denied.");
            return;
        }

        System.out.println("✅ Login successful. Welcome, " + user.fullName);
        int choice;
        do {
            System.out.println("\n==== MENU ====");
            System.out.println("1. Update Profile");
            System.out.println("2. Start Exam");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    updateProfile(user);
                    break;
                case 2:
                    Exam exam = new Exam();
                    exam.start();
                    break;
                case 3:
                    System.out.println("👋 Logged out successfully.");
                    break;
                default:
                    System.out.println("❗ Invalid option.");
            }
        } while (choice != 3);
    }

    void updateProfile(User user) {
        System.out.print("Enter new full name: ");
        String newName = sc.nextLine();
        System.out.print("Enter new password: ");
        String newPass = sc.nextLine();
        user.updateProfile(newName, newPass);
        System.out.println("✅ Profile updated successfully.");
    }
}

public class OnlineExamSystem {

    public static void main(String[] args) {
        ExamSystem system = new ExamSystem();
        system.run();
    }
}
