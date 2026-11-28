import java.util.ArrayList;
import java.util.Objects;

/**
 * Coordinates the department's students, faculty, programmes, and modules.
 */
public class Department {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Faculty> faculties = new ArrayList<>();
    private final ArrayList<Programme> programmes = new ArrayList<>();
    private final ArrayList<Module> modules = new ArrayList<>();

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public ArrayList<Faculty> getFaculties() {
        return new ArrayList<>(faculties);
    }

    public ArrayList<Programme> getProgrammes() {
        return new ArrayList<>(programmes);
    }

    public ArrayList<Module> getModules() {
        return new ArrayList<>(modules);
    }

    void transferLoadedStateFrom(Department source) {
        if (source == null || source == this || !isEmpty()) {
            throw new IllegalArgumentException("Loaded state cannot be transferred.");
        }
        students.addAll(source.students);
        faculties.addAll(source.faculties);
        programmes.addAll(source.programmes);
        modules.addAll(source.modules);
        source.students.clear();
        source.faculties.clear();
        source.programmes.clear();
        source.modules.clear();
    }

    public Student findStudent(String id) {
        for (Student student : students) {
            if (Objects.equals(student.getPersonId(), id)) {
                return student;
            }
        }
        return null;
    }

    public Faculty findFaculty(String id) {
        for (Faculty faculty : faculties) {
            if (Objects.equals(faculty.getPersonId(), id)) {
                return faculty;
            }
        }
        return null;
    }

    public Programme findProgramme(String code) {
        for (Programme programme : programmes) {
            if (Objects.equals(programme.getCode(), code)) {
                return programme;
            }
        }
        return null;
    }

    public Module findModule(String code) {
        for (Module module : modules) {
            if (Objects.equals(module.getCode(), code)) {
                return module;
            }
        }
        return null;
    }

    public Faculty findFacultyForModule(String moduleCode) {
        for (Faculty faculty : faculties) {
            Module taughtModule = faculty.getModule();
            if (taughtModule != null
                    && Objects.equals(taughtModule.getCode(), moduleCode)) {
                return faculty;
            }
        }
        return null;
    }

    public void addStudent(Student student) {
        if (student == null || student.getPersonId() == null
                || findStudent(student.getPersonId()) != null) {
            throw new IllegalArgumentException("Student is null or already exists.");
        }
        students.add(student);
    }

    public void addFaculty(Faculty faculty) {
        if (faculty == null || faculty.getPersonId() == null
                || findFaculty(faculty.getPersonId()) != null) {
            throw new IllegalArgumentException("Faculty is null or already exists.");
        }
        faculties.add(faculty);
    }

    public void addFaculty(Faculty faculty, Module module) {
        if (faculty == null || faculty.getPersonId() == null
                || findFaculty(faculty.getPersonId()) != null) {
            throw new IllegalArgumentException("Faculty is null or already exists.");
        }
        Module registeredModule = module == null ? null : findModule(module.getCode());
        if (registeredModule == null) {
            throw new IllegalArgumentException("Module is not registered with this department.");
        }
        if (findFacultyForModule(registeredModule.getCode()) != null) {
            throw new IllegalArgumentException("Module already has a faculty member.");
        }
        faculty.chooseAModuleToTeach(registeredModule);
        faculties.add(faculty);
    }

    public void assignFacultyToModule(Faculty faculty, Module module) {
        if (faculty == null || findFaculty(faculty.getPersonId()) != faculty) {
            throw new IllegalArgumentException("Faculty is not registered with this department.");
        }
        Module registeredModule = module == null ? null : findModule(module.getCode());
        if (registeredModule == null) {
            throw new IllegalArgumentException("Module is not registered with this department.");
        }
        Faculty assignedFaculty = findFacultyForModule(registeredModule.getCode());
        if (assignedFaculty != null && assignedFaculty != faculty) {
            throw new IllegalArgumentException("Module already has a faculty member.");
        }
        faculty.chooseAModuleToTeach(registeredModule);
    }

    public void addProgramme(Programme programme) {
        if (programme == null || programme.getCode() == null
                || findProgramme(programme.getCode()) != null) {
            throw new IllegalArgumentException("Programme is null or already exists.");
        }
        for (Module module : programme.getModules()) {
            if (findModule(module.getCode()) != module) {
                throw new IllegalArgumentException(
                        "Programme contains a module not registered with this department.");
            }
        }
        programme.attachToDepartment(this);
        programmes.add(programme);
    }

    public void addModule(Module module) {
        if (module == null || module.getCode() == null
                || findModule(module.getCode()) != null) {
            throw new IllegalArgumentException("Module is null or already exists.");
        }
        modules.add(module);
    }

    public void registerStudent(Student student, Programme programme) {
        if (student == null || findStudent(student.getPersonId()) != student) {
            throw new IllegalArgumentException("Student is not registered with this department.");
        }
        Programme registeredProgramme =
                programme == null ? null : findProgramme(programme.getCode());
        if (registeredProgramme == null || registeredProgramme != programme) {
            throw new IllegalArgumentException(
                    "Programme is not registered with this department.");
        }
        student.registerOnProgram(registeredProgramme);
    }

    public double calculateSemesterQCA(Student student) {
        return requireStudent(student).calculateSemesterQCA();
    }

    public double calculateOverallQCA(Student student) {
        return requireStudent(student).calculateOverallQCA();
    }

    public String identifyProgressionStatus(Student student) {
        return requireStudent(student).identifyProgressionStatus();
    }

    public void progress(Student student) {
        requireStudent(student).progress();
    }

    private Student requireStudent(Student student) {
        if (student == null || findStudent(student.getPersonId()) != student) {
            throw new IllegalArgumentException("Student is not registered with this department.");
        }
        return student;
    }

    private boolean isEmpty() {
        return students.isEmpty() && faculties.isEmpty()
                && programmes.isEmpty() && modules.isEmpty();
    }
}
