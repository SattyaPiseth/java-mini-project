package co.istad.mini_project.view;

import co.istad.mini_project.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentView {
    private final Scanner scanner;

    public StudentView() {
        scanner = new Scanner(System.in);
    }

    public void displayStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void displayPageInfo(Integer currentPage, Integer totalPages) {
        System.out.println("Page " + currentPage + " of " + totalPages);
    }

    public Student getStudentInfoFromUser() {
        System.out.print("Enter student id: ");
        Integer id = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student date of birth (YYYY-MM-DD): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        List<String> classroom = getStringListFromUser("Enter student classroom (comma-separated): ");
        List<String> subject = getStringListFromUser("Enter student subject (comma-separated): ");
        return new Student(id, name, dateOfBirth, classroom, subject, LocalDate.now());
    }

    private List<String> getStringListFromUser(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        String[] items = input.split(",");
        List<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item.trim());
        }
        return list;
    }

    public Student updateStudentInfoFromUser(Integer id) {
        try {
            System.out.print("Enter student name: ");
            scanner.nextLine(); // Consume newline character
            String name = scanner.nextLine();
            System.out.print("Enter student date of birth (YYYY-MM-DD): ");
            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
            List<String> classroom = getStringListFromUser("Enter student classroom (comma-separated): ");
            List<String> subject = getStringListFromUser("Enter student subject (comma-separated): ");
            return new Student(id, name, dateOfBirth, classroom, subject, LocalDate.now());
        } catch (InputMismatchException e) {
            System.err.println("Error: Invalid input. Please enter valid data.");
            return null;
        }
    }

    public String getSearchKeywordFromUser() {
        System.out.print("Enter search keyword: ");
        return scanner.next();
    }

    public void notifyError(String message) {
        System.err.println("Error: " + message);
    }

    public void displayMenu() {
        System.out.println("""
            =======================================================================================
            1. ADD NEW STUDENT              2. LIST ALL STUDENTS            3. COMMIT DATA TO FILE
            4. SEARCH FOR STUDENT           5. UPDATE STUDENT'S INFO BY ID  6. DELETE STUDENT'S DATA
            7. GENERATE DATA TO FILE        8. DELETE/CLEAR ALL DATA FROM DATA STORE
            0,99. EXIT
            =======================================================================================
            """);
        System.out.print("> Insert option: ");
    }

    public Integer getMenuOptionFromUser() {
        return scanner.nextInt();
    }
}
