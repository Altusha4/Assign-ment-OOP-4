import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class FitnessApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = FitnessAppDB.getConnection()) {
            // Создаём таблицы, если их нет
            FitnessAppDB.createTables(conn);

            // Ввод данных пользователя
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            int age = getValidIntInput(scanner, "Enter your age: ");
            double weight = getValidDoubleInput(scanner, "Enter your weight: ");

            User user = new User(name, age, weight);
            int userId = FitnessAppDB.insertUser(conn, user); // Сохраняем пользователя в базе данных

            System.out.println("\nWelcome to FitnessApp!");

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
                    case 1 -> addWorkoutRoutine(scanner, conn, userId); // Добавление тренировки
                    case 2 -> viewAllWorkoutRoutines(conn, userId); // Просмотр всех тренировок
                    case 3 -> manageUserInfo(scanner, conn, userId); // Управление информацией пользователя
                    case 4 -> addParameters(scanner, conn, userId); // Добавление параметров тела
                    case 5 -> viewParameters(conn, userId); // Просмотр параметров тела
                    case 6 -> {
                        System.out.println("Goodbye!");
                        return; // Завершение программы
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для добавления тренировки
    private static void addWorkoutRoutine(Scanner scanner, Connection conn, int userId) {
        System.out.println("Choose routine type:");
        System.out.println("1. Cardio");
        System.out.println("2. Strength");
        int routineTypeChoice = getValidIntInput(scanner, "Enter your choice: ");

        String routineType;
        switch (routineTypeChoice) {
            case 1 -> routineType = "Cardio";
            case 2 -> routineType = "Strength";
            default -> {
                System.out.println("Invalid choice. Defaulting to Cardio.");
                routineType = "Cardio";
            }
        }

        System.out.print("Enter routine name: ");
        String routineName = scanner.nextLine();
        int duration = getValidIntInput(scanner, "Enter duration (minutes): ");
        int calories = getValidIntInput(scanner, "Enter calories burned: ");

        WorkoutRoutine routine = new WorkoutRoutine(routineName, duration, calories, routineType);
        try {
            FitnessAppDB.insertWorkout(conn, userId, routine);
            System.out.println("Routine added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding routine: " + e.getMessage());
        }
    }

    // Метод для просмотра всех тренировок
    private static void viewAllWorkoutRoutines(Connection conn, int userId) {
        System.out.println("\nAll Routines:");
        try {
            FitnessAppDB.readWorkoutsByUser(conn, userId);
        } catch (SQLException e) {
            System.out.println("Error reading workouts: " + e.getMessage());
        }
    }

    // Метод для управления информацией о пользователе
    private static void manageUserInfo(Scanner scanner, Connection conn, int userId) {
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        int newAge = getValidIntInput(scanner, "Enter new age: ");
        double newWeight = getValidDoubleInput(scanner, "Enter new weight: ");

        try {
            FitnessAppDB.updateUser(conn, userId, newName, newAge, newWeight);
            System.out.println("User info updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating user info: " + e.getMessage());
        }
    }

    // Метод для добавления параметров тела
    private static void addParameters(Scanner scanner, Connection conn, int userId) {
        Date date = getValidDateInput(scanner, "Enter date (YYYY-MM-DD): ");
        double height = getValidDoubleInput(scanner, "Enter height (cm): ");
        double paramWeight = getValidDoubleInput(scanner, "Enter weight (kg): ");
        double chest = getValidDoubleInput(scanner, "Enter chest circumference (cm): ");
        double waist = getValidDoubleInput(scanner, "Enter waist circumference (cm): ");
        double hips = getValidDoubleInput(scanner, "Enter hips circumference (cm): ");

        try {
            FitnessAppDB.insertParameters(conn, userId, date, height, paramWeight, chest, waist, hips);
            System.out.println("Parameters added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding parameters: " + e.getMessage());
        }
    }

    // Метод для просмотра параметров тела
    private static void viewParameters(Connection conn, int userId) {
        System.out.println("\nUser Parameters:");
        try {
            FitnessAppDB.readParameters(conn, userId);
        } catch (SQLException e) {
            System.out.println("Error reading parameters: " + e.getMessage());
        }
    }

    // Получение и проверка даты
    private static Date getValidDateInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

    // Получение и проверка целого числа
    private static int getValidIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    // Получение и проверка числа с плавающей точкой
    private static double getValidDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
