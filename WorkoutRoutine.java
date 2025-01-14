public class WorkoutRoutine {
    private String routineName;
    private int durationInMinutes;
    private int caloriesBurned;
    private String routineType; // Тип тренировки (Cardio/Strength)

    public WorkoutRoutine(String routineName, int durationInMinutes, int caloriesBurned, String routineType) {
        this.routineName = routineName;
        this.durationInMinutes = durationInMinutes;
        this.caloriesBurned = caloriesBurned;
        this.routineType = routineType;
    }

    public String getRoutineName() {
        return routineName;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getRoutineType() {
        return routineType;
    }

    public double getCaloriesPerMinute() {
        return (double) caloriesBurned / durationInMinutes;
    }

    @Override
    public String toString() {
        return String.format("Routine: %-20s | Type: %-10s | Duration: %3d min | Calories: %4d kcal",
                routineName, routineType, durationInMinutes, caloriesBurned);
    }
}
