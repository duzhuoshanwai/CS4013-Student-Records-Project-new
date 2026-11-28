import java.io.IOException;

/**
 * Defines persistence operations for application data and module grades.
 */
public interface Storable {
    /** Loads persisted application data into the Department. */
    void read(Department department) throws IOException;

    /** Writes the current Department state. */
    void write(Department department) throws IOException;

    /** Persists the student's current grade for the specified module. */
    void update(Student student, Module module) throws IOException;
}
