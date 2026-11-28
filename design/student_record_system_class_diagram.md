# CS4013 Student Record System – UML Class Diagram

```mermaid
classDiagram
    direction TB

    class Main {
        +run(UserInterface) void
    }

    class Department {
        -ArrayList~Student~ students
        -ArrayList~Faculty~ faculties
        -ArrayList~Programme~ programmes
        -ArrayList~Module~ modules
        +Department()
        +getStudents() ArrayList~Student~
        +getFaculties() ArrayList~Faculty~
        +getProgrammes() ArrayList~Programme~
        +getModules() ArrayList~Module~
        +findStudent(id: String) Student
        +findFaculty(id: String) Faculty
        +findProgramme(code: String) Programme
        +findModule(code: String) Module
        +findFacultyForModule(moduleCode: String) Faculty
        +addStudent(student: Student) void
        +addFaculty(faculty: Faculty) void
        +addFaculty(faculty: Faculty, module: Module) void
        +assignFacultyToModule(faculty: Faculty, module: Module) void
        +addProgramme(programme: Programme) void
        +addModule(module: Module) void
        +registerStudent(student: Student, programme: Programme) void
        +calculateSemesterQCA(student: Student) double
        +calculateOverallQCA(student: Student) double
        +identifyProgressionStatus(student: Student) String
        +progress(student: Student) void
    }

    class Person {
        <<abstract>>
        -String name
        -String personId
        +generateTranscript()* String
    }

    class Student {
        -Programme programme
        -String progressionStatus
        -int currentYear
        -int currentSemester
        +Student(name, studentId)
        +registerOnProgram(programme) void
        +receiveGrade(grade, module) void
        +calculateSemesterQCA() double
        +calculateOverallQCA() double
        +identifyProgressionStatus() String
        +progress() void
        +generateTranscript() String
    }

    class Faculty {
        -Module module
        -ArrayList~Student~ students
        +Faculty(name, personId, storable)
        +getModule() Module
        +chooseAModuleToTeach(module) void
        +setResult(student, grade) void
        +getStudents() ArrayList~Student~
        +generateTranscript() String
    }

    class Module {
        -String name
        -String code
        -Grade grade
        +Module(code, name)
        +setGrade(Grade) void
        +copy() Module
    }

    class Programme {
        -String code
        -String name
        -Department department
        -ArrayList~Module~ modules
        -Map~String, Integer~ moduleSemesters
        +Programme(code, name)
        +addModule(module, semester) void
        +getModules() ArrayList~Module~
        +getModuleSemester(moduleCode) int
        +copy() Programme
    }

    class Grade {
        <<enumeration>>
        A1
        A2
        B1
        B2
        B3
        C1
        C2
        C3
        D1
        F
        P
        NG
        N
        -double qvp
        +getQvp() double
    }

    class CLI_class {
        +PlaceHolder() void
    }

    class UserInterface {
        <<interface>>
        +PlaceHolder() void
    }

    class CSV_handler {
        -Path dataDirectory
        -Department department
        +read(department) void
        +write(department) void
        +update(student, module) void
    }

    class Storable {
        <<interface>>
        +read(department) void
        +write(department) void
        +update(student, module) void
    }

    %% Inheritance
    Person <|-- Student : extends
    Person <|-- Faculty : extends

    %% Interface implementation
    UserInterface <|.. CLI_class : implements
    Storable <|.. CSV_handler : implements

    %% Dependencies
    Main --> Department
    Main ..> CSV_handler : loads and saves Department state
    Main ..> UserInterface : uses
    Faculty ..> Storable : writes module result CSV

    %% Department holds the canonical objects
    Department "1" o--> "0..*" Faculty
    Department "1" o--> "0..*" Student
    Department "1" o--> "0..*" Programme
    Department "1" o--> "0..*" Module

    %% Faculty
    Faculty "0..1" --> "0..1" Module : assigned
    Faculty "0..*" -- "0..*" Student : grades

    %% Student registration copies the Programme and its Modules.
    Student "1" *--> "0..1" Programme
    %% Catalogue Programmes reference Department's canonical Module objects.
    Programme "0..*" --> "0..*" Module : references
```

## Container model

Each `Student` owns a copy of their `Programme`, which owns copies of its `Module` objects:

`Student[Programme[Module1[Grade?], Module2[Grade?], ...]]`

- `Department` owns the shared programme/module catalog in memory.
- `CSV_handler` loads records into an empty Department and persists the current
  Department state; it may keep a reference to that Department for immediate
  grade updates, but does not keep a second copy of the entity collections.
- `Student.registerOnProgram(programme)` copies the programme, its module-semester mapping, and its modules. A copied module starts with a null grade; an explicit `NG` is a recorded grade.
- `Faculty.setResult(student, grade)` checks that the student's programme contains the taught module, then records the grade on that student's module copy.
- The module result CSV stores one current grade per student and module; there is no result ID, attempt history, or result-specific semester.
- Semester numbers increase across the whole course. `calculateSemesterQCA()` uses `currentSemester` and the copied programme's module-semester mapping; `generateTranscript()` lists recorded grades with their curriculum semesters.
- Handbook `NG` has 0.00 QPV and is included in QCA; ungraded null grades are excluded. Pass/fail `P` and `N` are non-quality grades.
- `Department.identifyProgressionStatus(student)` sets the student's status to `PROGRESS` when their overall QCA is at least 2.00, and to `REPEAT` otherwise.
- `Department.progress(student)` advances the student to the next semester only when their progression status is `PROGRESS`.

## Relationship summary

| From | Relationship | To | Multiplicity |
|---|---|---|---|
| Student | Inheritance (extends) | Person | – |
| Faculty | Inheritance (extends) | Person | – |
| CLI_class | Realization (implements) | UserInterface | – |
| CSV_handler | Realization (implements) | Storable | – |
| Main | Association (uses) | Department | – |
| Main | Dependency (uses) | CLI_class | – |
| Faculty | Dependency (uses) | Storable | – |
| Department | Aggregation | Faculty | 1 Department to 0..* Faculty |
| Department | Aggregation | Student | 1 Department to 0..* Student |
| Department | Aggregation | Programme | 1 Department to 0..* Programme |
| Department | Aggregation | Module | 1 Department to 0..* Module |
| Faculty | Association | Module | 0..1 per Faculty; at most 1 Faculty per Module |
| Faculty | Association | Student | 0..* per Faculty |
| Student | Composition | Programme copy | 0..1 per Student |
| Programme | Association (references catalogue modules) | Module | 0..* per Programme; modules may be shared |