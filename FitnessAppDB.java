import java.sql.*;

public class FitnessAppDB {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/fitnessdb";
    private static final String USER = "postgres";
    private static final String PASS = "Altusha006";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found!");
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void createTables(Connection conn) throws SQLException {
        String createUsersTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                age INT NOT NULL,
                weight DECIMAL(5, 2) NOT NULL
            );
        """;

        String createWorkoutTableSQL = """
            CREATE TABLE IF NOT EXISTS workout_routines (
                id SERIAL PRIMARY KEY,
                user_id INT REFERENCES users(id) ON DELETE CASCADE,
                routine_name VARCHAR(100) NOT NULL,
                duration_minutes INT NOT NULL,
                calories_burned INT NOT NULL,
                routine_type VARCHAR(50) NOT NULL
            );
        """;

        String createParametersTableSQL = """
            CREATE TABLE IF NOT EXISTS parameters (
                id SERIAL PRIMARY KEY,
                user_id INT REFERENCES users(id) ON DELETE CASCADE,
                date DATE NOT NULL,
                height DECIMAL(5, 2),
                weight DECIMAL(5, 2),
                chest DECIMAL(5, 2),
                waist DECIMAL(5, 2),
                hips DECIMAL(5, 2)
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSQL);
            stmt.execute(createWorkoutTableSQL);
            stmt.execute(createParametersTableSQL);
        }
    }

    public static int insertUser(Connection conn, User user) throws SQLException {
        String insertUserSQL = """
            INSERT INTO users (name, age, weight) VALUES (?, ?, ?) RETURNING id;
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getAge());
            pstmt.setDouble(3, user.getWeight());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Failed to insert user.");
    }

    public static void updateUser(Connection conn, int userId, String name, int age, double weight) throws SQLException {
        String updateUserSQL = """
            UPDATE users
            SET name = ?, age = ?, weight = ?
            WHERE id = ?;
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(updateUserSQL)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setDouble(3, weight);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
        }
    }

    public static void insertWorkout(Connection conn, int userId, WorkoutRoutine routine) throws SQLException {
        String insertWorkoutSQL = """
            INSERT INTO workout_routines (user_id, routine_name, duration_minutes, calories_burned, routine_type)
            VALUES (?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertWorkoutSQL)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, routine.getRoutineName());
            pstmt.setInt(3, routine.getDurationInMinutes());
            pstmt.setInt(4, routine.getCaloriesBurned());
            pstmt.setString(5, routine.getRoutineType());
            pstmt.executeUpdate();
        }
    }

    public static void readWorkoutsByUser(Connection conn, int userId) throws SQLException {
        String query = "SELECT * FROM workout_routines WHERE user_id = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("ID: %d | Name: %s | Type: %s | Duration: %d min | Calories: %d kcal%n",
                            rs.getInt("id"),
                            rs.getString("routine_name"),
                            rs.getString("routine_type"),
                            rs.getInt("duration_minutes"),
                            rs.getInt("calories_burned"));
                }
            }
        }
    }

    public static void insertParameters(Connection conn, int userId, Date date, double height, double weight, double chest, double waist, double hips) throws SQLException {
        String insertParametersSQL = """
            INSERT INTO parameters (user_id, date, height, weight, chest, waist, hips)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(insertParametersSQL)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, date);
            pstmt.setDouble(3, height);
            pstmt.setDouble(4, weight);
            pstmt.setDouble(5, chest);
            pstmt.setDouble(6, waist);
            pstmt.setDouble(7, hips);
            pstmt.executeUpdate();
        }
    }

    public static void readParameters(Connection conn, int userId) throws SQLException {
        String query = "SELECT * FROM parameters WHERE user_id = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("Date: %s | Height: %.2f | Weight: %.2f | Chest: %.2f | Waist: %.2f | Hips: %.2f%n",
                            rs.getDate("date"),
                            rs.getDouble("height"),
                            rs.getDouble("weight"),
                            rs.getDouble("chest"),
                            rs.getDouble("waist"),
                            rs.getDouble("hips"));
                }
            }
        }
    }
}
