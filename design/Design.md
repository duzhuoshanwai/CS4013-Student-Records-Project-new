# Design — Student Record System

## List of Classes

**Domain / core classes**

- **Main** — runs the program
- **Department** — holds exam boards, calculates QCA…
- **Student** — can view the transcript
- **Faculty** — can look up transcript and upload results (writes to `results.csv`)
- **«abstract» Person** — Student and Faculty inherit from Person
- **Program**
- **Module**
- **«enum» Grade**

**Infrastructure / interface layer**

- **CLI_class**
- **CSV_handler** — to read, write and update CSV
- **CLI interface `Presentable`** — `CLI_class` implements `Presentable`
- **CSV interface `Storable`** — `CSV_handler` implements `Storable`

---

## Core Requirements

| ID | Requirement |
|----|-------------|
| R1 | Add student(s) |
| R2 | Add program |
| R3 | Add module |
| R4 | Register student on program |
| R5 | Record module results |
| R6 | Calculate semester QCA |
| R7 | Calculate overall QCA |
| R8 | Generate transcript |
| R9 | Identify progression status |

---

## Core Requirements Coverage Map

| ID | Requirement | Class |
|----|-------------|-------|
| R1 | Add student(s) | Department |
| R2 | Add program | Department |
| R3 | Add module | Program (to this program), Department (just add standalone module) |
| R4 | Register student on programme | Student (contains implementation), Department (invokes method) |
| R5 | Record module results | Faculty (invokes method and passes grade `{A1, A2, B1, B2, B3 …}` as parameter), Student (has method implementation) |
| R6 | Calculate semester QCA | Student (has implementation), Department (invokes the method) |
| R7 | Calculate overall QCA | Student (has implementation), Department (invokes the method) |
| R8 | Generate transcript | Student (can generate own transcript only), Faculty (can generate transcript for their module) |
| R9 | Identify progression status | Student (contains implementation), Department (invokes method) |

---

## Additional Requirements Needed by Design

| ID | Requirement |
|----|-------------|
| A1 | Add faculty for the specific module |
| A2 | Progress student to next semester |

### Additional Requirements Coverage Map

| ID | Requirement | Class |
|----|-------------|-------|
| A1 | Add faculty for the specific module | Department |
| A2 | Progress student to next semester | Department (if the student's progression status is "progress"), Student (contains implementation) |

---

## Assumptions

- Professor (Faculty) can teach only **one module at a time**. Faculty can choose what module to teach only if they currently do not teach any student (when Faculty teaches).
- Faculty has `ArrayList<Student>` because they need to grade every individual student. A professor cannot put a single grade for the whole module (class); each Student receives their own grade.
- There is a differentiation between **Object** and **Role**:
  - We have 3 roles: `DEPARTMENT`, `FACULTY`, `STUDENT`
  - and we have objects: `student1`, `student2`, `faculty1`, `faculty2`, …
  - The `Student` class should have lots of methods (calculate QCA), but the `STUDENT` role has limited features (only view their own transcript).

---
