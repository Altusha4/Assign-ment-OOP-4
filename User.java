import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private int age;
    private double weight;
    private List<WorkoutRoutine> routines;

    public User(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.routines = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<WorkoutRoutine> getRoutines() {
        return routines;
    }

    public void addRoutine(WorkoutRoutine routine) {
        routines.add(routine);
    }

    public void removeRoutine(String routineName) {
        routines.removeIf(r -> r.getRoutineName().equalsIgnoreCase(routineName));
    }

    @Override
    public String toString() {
        return String.format("User Info:\nName: %s | Age: %d | Weight: %.2f kg", name, age, weight);
    }
}
