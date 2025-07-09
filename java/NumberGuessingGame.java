
import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();
    static final int MAX_ATTEMPTS = 7; // attempts allowed per round
    static final int RANGE = 100;       // 1 to 100

    public static void main(String[] args) {
        System.out.println("🎯 Welcome to the Number Guessing Game!");
        System.out.print("Enter your name: ");
        String playerName = sc.nextLine();

        int rounds;
        System.out.print("Enter number of rounds you want to play: ");
        rounds = getValidIntegerInput();

        int totalScore = 0;

        for (int round = 1; round <= rounds; round++) {
            System.out.println("\n🔁 Round " + round + " starts now!");
            int target = random.nextInt(RANGE) + 1;
            int attempts = 0;
            boolean guessedCorrect = false;

            while (attempts < MAX_ATTEMPTS) {
                System.out.print("Enter your guess (1 to " + RANGE + "): ");
                int guess = getValidIntegerInput();
                attempts++;

                if (guess == target) {
                    System.out.println("✅ Correct! You've guessed the number in " + attempts + " attempts.");
                    int roundScore = (MAX_ATTEMPTS - attempts + 1) * 10; // more points for fewer attempts
                    System.out.println("🎉 You earned " + roundScore + " points this round.");
                    totalScore += roundScore;
                    guessedCorrect = true;
                    break;
                } else if (guess < target) {
                    System.out.println("🔼 Too low!");
                } else {
                    System.out.println("🔽 Too high!");
                }
            }

            if (!guessedCorrect) {
                System.out.println("❌ You've used all attempts! The correct number was: " + target);
            }
        }

        System.out.println("\n🏁 Game Over!");
        System.out.println("👤 Player: " + playerName);
        System.out.println("📊 Total Score: " + totalScore);
        System.out.println("Thanks for playing!");
    }

    // Utility method to safely get integer input from user
    private static int getValidIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❗ Invalid input. Please enter a number: ");
            }
        }
    }
}
