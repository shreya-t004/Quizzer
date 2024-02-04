import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String TEST_DATA_FILE = "mcq_test_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Test");
            System.out.println("2. Take Test");
            System.out.println("3. Correct Test");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createTest();
                    break;
                case 2:
                    takeTest();
                    break;
                case 3:
                    correctTest();
                    break;
                case 4:
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createTest() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DATA_FILE, true))) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter the test details:");

            System.out.print("Test Name: ");
            String testName = scanner.nextLine();

            writer.println(testName);

            System.out.print("Enter the number of questions: ");
            int numQuestions = scanner.nextInt();
            writer.println(numQuestions);

            scanner.nextLine(); 

            for (int i = 1; i <= numQuestions; i++) {
                System.out.print("Enter Question " + i + ": ");
                String question = scanner.nextLine();
                writer.println(question);

                System.out.print("Enter Options (comma-separated): ");
                String options = scanner.nextLine();
                writer.println(options);

                System.out.print("Enter Correct Answer: ");
                String correctAnswer = scanner.nextLine();
                writer.println(correctAnswer);
            }

            System.out.println("Test created successfully!");
        } catch (IOException e) {
            System.err.println("Error creating the test: " + e.getMessage());
        }
    }

    private static void takeTest() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_DATA_FILE))) {
            System.out.println("Available Tests:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter the test name to take: ");
            String testName = scanner.nextLine();

            
            boolean testFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals(testName)) {
                    testFound = true;
                    int numQuestions = Integer.parseInt(reader.readLine());

                    ArrayList<String> correctAnswers = new ArrayList<>();
                    for (int i = 1; i <= numQuestions; i++) {
                        System.out.println("Question " + i + ": " + reader.readLine());
                        System.out.println("Options: " + reader.readLine());

                        String correctAnswer = reader.readLine();
                        correctAnswers.add(correctAnswer);

                        System.out.print("Your Answer: ");
                        String userAnswer = scanner.nextLine();
                        System.out.println("Your Answer: " + userAnswer);
                    }

                    System.out.println("Test taken successfully!");

                    recordTestAnswers(testName, correctAnswers);
                    break;
                } else {
                    int numQuestions = Integer.parseInt(reader.readLine());
                    for (int i = 0; i < numQuestions * 3; i++) {
                        reader.readLine();
                    }
                }
            }

            if (!testFound) {
                System.out.println("Test not found!");
            }
        } catch (IOException e) {
            System.err.println("Error taking the test: " + e.getMessage());
        }
    }

    private static void recordTestAnswers(String testName, ArrayList<String> correctAnswers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(testName + "_answers.txt"))) {
            for (String answer : correctAnswers) {
                writer.println(answer);
            }
        } catch (IOException e) {
            System.err.println("Error recording test answers: " + e.getMessage());
        }
    }

    private static void correctTest() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the test name to correct: ");
        String testName = scanner.nextLine();

        try (BufferedReader correctReader = new BufferedReader(new FileReader(testName + "_answers.txt"));
             BufferedReader userReader = new BufferedReader(new FileReader(testName + "_user_answers.txt"))) {

            ArrayList<String> correctAnswers = new ArrayList<>();
            ArrayList<String> userAnswers = new ArrayList<>();

            String line;
            while ((line = correctReader.readLine()) != null) {
                correctAnswers.add(line);
            }

            while ((line = userReader.readLine()) != null) {
                userAnswers.add(line);
            }

            int numQuestions = correctAnswers.size();
            int correctCount = 0;

            System.out.println("Correcting test...");

            for (int i = 0; i < numQuestions; i++) {
                System.out.println("Question " + (i + 1) + ":");
                System.out.println("Correct Answer: " + correctAnswers.get(i));
                System.out.println("Your Answer   : " + userAnswers.get(i));

                if (correctAnswers.get(i).equalsIgnoreCase(userAnswers.get(i))) {
                    correctCount++;
                }
            }

            System.out.println("Correction completed!");
            System.out.println("Score: " + correctCount + "/" + numQuestions);

        } catch (IOException e) {
            System.err.println("Error correcting the test: " + e.getMessage());
        }
    }
}
