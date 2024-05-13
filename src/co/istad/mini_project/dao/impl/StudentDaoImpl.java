package co.istad.mini_project.dao.impl;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDaoImpl implements StudentDao {
    private static final String PRIMARY_DATA_PATH = "primary_data.txt";
    private static final String ADD_TRANSACTION_PATH = "transaction_add.txt";
    private static final String UPDATE_TRANSACTION_PATH = "transaction_update.txt";
    private static final String DELETE_TRANSACTION_PATH = "transaction_delete.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final List<Student> students = new ArrayList<>();


    public StudentDaoImpl() {
        loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRIMARY_DATA_PATH, StandardCharsets.UTF_8))) {
            students.addAll(reader.lines()
                    .map(this::parseStudent)
                    .filter(Objects::nonNull)  // Filter out null values
                    .toList());
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }

    private Student parseStudent(String line) {
        if (line == null || line.trim().isEmpty()) {
            System.err.println("Empty line encountered in the file.");
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length < 6) {
            System.err.println("Invalid student data: " + line);
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            LocalDate dateOfBirth = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern(DATE_FORMAT));
            List<String> classroom = parseList(parts[3]);
            List<String> subject = parseList(parts[4]);
            LocalDate date = LocalDate.parse(parts[5], DateTimeFormatter.ofPattern(DATE_FORMAT));
            return new Student(id, name, dateOfBirth, classroom, subject, date);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing student data: " + e.getMessage());
            return null;
        }
    }

    private List<String> parseList(String input) {
        return List.of(input.split(";"));
    }


    @Override
    public List<Student> getAllStudents() {
        try (Stream<String> lines = Files.lines(Paths.get(PRIMARY_DATA_PATH), StandardCharsets.UTF_8)) {
            return lines.map(this::parseStudent)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(Student::getId)) // Sort by student ID
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void addStudent(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADD_TRANSACTION_PATH, StandardCharsets.UTF_8, true))) {
            String csvString = studentToCsvString(student);
            writer.write(csvString);
            writer.newLine(); // Append a new line after writing the CSV string
        } catch (IOException e) {
            throw new RuntimeException("Error updating student data: " + e.getMessage(), e);
        }
    }


    private void saveToFile(List<Student> data, String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Student student : data) {
            lines.add(studentToCsvString(student));
        }
        Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
    }



    @Override
    public void updateStudent(Student student) {
        // get student to be updated write in file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UPDATE_TRANSACTION_PATH, StandardCharsets.UTF_8))) {
            writer.write(studentToCsvString(student));
        } catch (IOException e) {
            throw new RuntimeException("Error updating student data: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        // get student to be updated write in file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DELETE_TRANSACTION_PATH, StandardCharsets.UTF_8))) {
            writer.write(studentToCsvString(getStudentById(id).get()));
        } catch (IOException e) {
            throw new RuntimeException("Error updating student data: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> searchStudents(String keyword) throws IOException {
        String lowercaseKeyword = keyword.toLowerCase();
        List<Student> primaryDataStudents = loadStudentsFromFile(PRIMARY_DATA_PATH);

        return primaryDataStudents.stream()
                .filter(student -> studentMatchesKeyword(student, lowercaseKeyword, keyword))
                .collect(Collectors.toList());
    }

    private boolean studentMatchesKeyword(Student student, String lowercaseKeyword, String keyword) {
        return student.getName().toLowerCase().contains(lowercaseKeyword) || student.getId().toString().equals(keyword);
    }

    private List<Student> loadStudentsFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        List<Student> students = new ArrayList<>();
        for (String line : lines) {
            Student student = parseStudent(line);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }
    @Override
    public Optional<Student> getStudentById(Integer id) throws IOException {

        return loadStudentsFromFile(PRIMARY_DATA_PATH)
                .stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    @Override
    public void commit() {
        try {
            // Load transaction data
            List<Student> addTransactions = loadStudentsFromFile(ADD_TRANSACTION_PATH);
            List<Student> updateTransactions = loadStudentsFromFile(UPDATE_TRANSACTION_PATH);
            List<Student> deleteTransactions = loadStudentsFromFile(DELETE_TRANSACTION_PATH);

            // Load current data
            List<Student> currentData = loadStudentsFromFile(PRIMARY_DATA_PATH);

            // Step 1: Remove students listed in deleteTransactions from currentData
            Set<Integer> deleteIds = deleteTransactions.stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            currentData.removeIf(student -> deleteIds.contains(student.getId()));

            // Step 2: Update existing students
            Map<Integer, Student> updateMap = updateTransactions.stream()
                    .collect(Collectors.toMap(Student::getId, Function.identity()));
            currentData.replaceAll(student -> updateMap.getOrDefault(student.getId(), student));

            // Step 3: Add new students (ensuring uniqueness)
            Set<Integer> currentIds = currentData.stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            addTransactions.stream()
                    .filter(student -> !currentIds.contains(student.getId()))
                    .forEach(currentData::add);

            // Step 4: Remove updated students from addTransactions and deleteTransactions
            addTransactions.removeIf(student -> updateMap.containsKey(student.getId()));
            deleteTransactions.removeIf(student -> updateMap.containsKey(student.getId()));

            // Step 5: Save updated data to primary data path
            saveToFile(currentData, PRIMARY_DATA_PATH);

            // Step 6: Clear transaction files
            clearTransactionFiles();
        } catch (IOException e) {
            throw new RuntimeException("Error committing data: " + e.getMessage(), e);
        }
    }

    private void clearTransactionFiles() throws IOException {
        Files.write(Paths.get(ADD_TRANSACTION_PATH), new ArrayList<>());
        Files.write(Paths.get(UPDATE_TRANSACTION_PATH), new ArrayList<>());
        Files.write(Paths.get(DELETE_TRANSACTION_PATH), new ArrayList<>());
    }

    private String studentToCsvString(Student student) {
        return String.format("%d,%s,%s,%s,%s,%s",
                student.getId(), student.getName(), student.getDateOfBirth(),
                String.join(";", student.getClassroom()), String.join(";", student.getSubject()), student.getDate());
    }
}
