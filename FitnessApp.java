import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FitnessApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = FitnessAppDB.getConnection()) {
            FitnessAppDB.createTables(conn);

            System.out.println("\n=== Welcome to FitnessApp ===");
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            int age = getValidIntInput(scanner, "Enter your age: ");
            double weight = getValidDoubleInput(scanner, "Enter your weight: ");

            User user = new User(name, age, weight);
            int userId = FitnessAppDB.insertUser(conn, user);

            System.out.println("\nUser created successfully!");
            System.out.println(user);

            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Add Workout Routine");
                System.out.println("2. View All Workout Routines");
                System.out.println("3. Manage User Info");
                System.out.println("4. Add Parameters");
                System.out.println("5. View Parameters");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = getValidIntInput(scanner, "");
                switch (choice) {
                    case 1 -> addWorkoutRoutine(scanner, conn, userId);
                    case 2 -> viewAllWorkoutRoutines(conn, userId);
                    case 3 -> manageUserInfo(scanner, conn, userId);
                    case 4 -> addParameters(scanner, conn, userId);
                    case 5 -> viewParameters(conn, userId);
                    case 6 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getValidIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double getValidDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static void addWorkoutRoutine(Scanner scanner, Connection conn, int userId) {
        System.out.println("Choose routine type: 1. Cardio  2. Strength");
        int typeChoice = getValidIntInput(scanner, "Enter choice: ");
        String routineType = (typeChoice == 1) ? "Cardio" : "Strength";

        System.out.print("Enter routine name: ");
        String routineName = scanner.nextLine();
        int duration = getValidIntInput(scanner, "Enter duration (minutes): ");
        int calories = getValidIntInput(scanner, "Enter calories burned: ");

        WorkoutRoutine routine = new WorkoutRoutine(routineName, duration, calories, routineType);
        try {
            FitnessAppDB.insertWorkout(conn, userId, routine);
            System.out.println("Workout added successfully!\n");
            viewAllWorkoutRoutines(conn, userId);
        } catch (SQLException e) {
            System.out.println("Error adding workout: " + e.getMessage());
        }
    }

    private static void viewAllWorkoutRoutines(Connection conn, int userId) {
        try {
            List<WorkoutRoutine> routines = FitnessAppDB.readWorkoutsByUser(conn, userId);
            System.out.println("\n=== Your Workout Routines ===");
            routines.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error reading workouts: " + e.getMessage());
        }
    }

    private static void manageUserInfo(Scanner scanner, Connection conn, int userId) {
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        int newAge = getValidIntInput(scanner, "Enter new age: ");
        double newWeight = getValidDoubleInput(scanner, "Enter new weight: ");

        try {
            FitnessAppDB.updateUser(conn, userId, newName, newAge, newWeight);
            System.out.println("User info updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating user info: " + e.getMessage());
        }
    }

    private static void addParameters(Scanner scanner, Connection conn, int userId) {
        Date date = new Date(System.currentTimeMillis());
        double height = getValidDoubleInput(scanner, "Enter height (cm): ");
        double weight = getValidDoubleInput(scanner, "Enter weight (kg): ");
        double chest = getValidDoubleInput(scanner, "Enter chest circumference (cm): ");
        double waist = getValidDoubleInput(scanner, "Enter waist circumference (cm): ");
        double hips = getValidDoubleInput(scanner, "Enter hips circumference (cm): ");

        try {
            FitnessAppDB.insertParameters(conn, userId, date, height, weight, chest, waist, hips);
            System.out.println("Parameters added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding parameters: " + e.getMessage());
        }
    }

    private static void viewParameters(Connection conn, int userId) {
        try {
            List<String> parameters = FitnessAppDB.readParametersByUser(conn, userId);
            System.out.println("\n=== User Parameters ===");
            parameters.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error reading parameters: " + e.getMessage());
        }
    }
}
