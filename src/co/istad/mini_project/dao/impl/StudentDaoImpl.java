package co.istad.mini_project.dao.impl;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Sattya
 * create at 5/5/2024 8:59 AM
 */
public class StudentDaoImpl implements StudentDao {
    private static final String TRANSACTION_FILE = "transaction_add.csv";
    private static final Object fileLock = new Object();
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 6) {
                    throw new IllegalArgumentException("Invalid data format for student: " + line);
                }
                students.add(parseStudent(data));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading student data from file: " + TRANSACTION_FILE, e);
        }

        return students;
    }
    private Student parseStudent(String[] data) {
        int id = Integer.parseInt(data[0]);
        String name = data[1];
        String dateOfBirth = data[2];
        String classroom = data[3];
        String subject = data[4];
        LocalDate date = LocalDate.parse(data[5]);

        return new Student(id,name,LocalDate.parse(dateOfBirth.formatted(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),classroom,subject,date);
    }

    @Override
    public void addStudent(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
            // Serialize the student object to a string
            String studentData = serializeStudent(student);

            // Acquire lock before writing to file
            synchronized (fileLock) {
                // Write data to file
                writer.write(studentData);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error adding student", e);
        }
    }

    private String serializeStudent(Student student) {
        // Serialize the student object to a string representation
        // You can use any serialization technique like JSON, XML, etc.
        // Here, for simplicity, let's assume concatenating strings is sufficient
        StringBuilder sb = new StringBuilder();
        sb.append(student.getId()).append(",");
        sb.append(student.getName()).append(",");
        sb.append(student.getDateOfBirth()).append(",");
        sb.append(student.getClassroom()).append(",");
        sb.append(student.getSubject()).append(",");
        sb.append(student.getDate());
        return sb.toString();
    }

    @Override
    public void updateStudent(Student student) {

    }

    @Override
    public void deleteStudent(Integer id) {

    }

    @Override
    public List<Student> searchStudents(String keyword) {
        return null;
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return Optional.empty();
    }
}
