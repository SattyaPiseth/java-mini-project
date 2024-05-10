package co.istad.mini_project.dao.impl;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {
    private static final String FILE_PATH = "transaction_add.csv";
    private final List<Student> students = new ArrayList<>();

    public StudentDaoImpl() {
        loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = parseStudent(line);
                if (student != null) {
                    students.add(student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }

    private Student parseStudent(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            System.err.println("Invalid student data: " + line);
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            LocalDate dateOfBirth = LocalDate.parse(parts[2]);
            String classroom = parts[3];
            String subject = parts[4];
            LocalDate date = LocalDate.parse(parts[5]);
            return new Student(id, name, dateOfBirth, classroom, subject, date);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.err.println("Error parsing student data: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write(studentToCsvString(student) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    @Override
    public void updateStudent(Student student) {
        Optional<Student> existingStudent = getStudentById(student.getId());
        if (existingStudent.isPresent()) {
            int index = students.indexOf(existingStudent.get());
            students.set(index, student);
            saveToFile();
        } else {
            System.err.println("Student not found with ID: " + student.getId());
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        students.removeIf(student -> student.getId().equals(id));
        saveToFile();
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().contains(keyword) || student.getId().toString().contains(keyword)) {
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    private String studentToCsvString(Student student) {
        return String.format("%d,%s,%s,%s,%s,%s",
                student.getId(), student.getName(), student.getDateOfBirth(),
                student.getClassroom(), student.getSubject(), student.getDate());
    }
}
