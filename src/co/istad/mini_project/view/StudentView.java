package co.istad.mini_project.view;

import co.istad.mini_project.model.Student;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * @author Sattya
 * create at 5/5/2024 9:00 AM
 */
public class StudentView {
    private final Scanner scanner;

    // Constructor
    public StudentView(){
        scanner = new Scanner(System.in);
    }

    public void displayStudents(List<Student> students){
        // Display all students
        for (Student student : students) {
            System.out.println(student);
        }

    }
    public void displayPageInfo(Integer currentPage, Integer totalPages){
        // Display page info
        System.out.println("Page " + currentPage + " of " + totalPages);
    }
    public Student getStudentInfoFromUser() {
        // Prompt user to enter student information and return a new Student object
        System.out.print("Enter student id: ");
        Integer id = scanner.nextInt();
        System.out.print("Enter student name: ");
        scanner.nextLine(); // Consume newline character
        String name = scanner.nextLine();
        System.out.print("Enter student date of birth (YYYY-MM-DD): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        List<String> classroom = getClassroomFromUser();
        List<String> subject = getSubjectFromUser();
        return new Student(id, name, dateOfBirth, classroom, subject, LocalDate.now());
    }

    private List<String> getClassroomFromUser() {
        // Prompt user to enter classroom information and return a list
        System.out.print("Enter student classroom (comma-separated): ");
        return getStrings();
    }

    private List<String> getSubjectFromUser() {
        // Prompt user to enter subject information and return a list
        System.out.print("Enter student subject (comma-separated): ");
        return getStrings();
    }

    private List<String> getStrings() {
        String input = scanner.nextLine();
        String[] subjects = input.split(",");
        List<String> subjectList = new ArrayList<>();
        for (String subject : subjects) {
            subjectList.add(subject.trim());
        }
        return subjectList;
    }

    public Student updateStudentInfoFromUser(Integer id) {
        try {
            System.out.print("Enter student name: ");
            scanner.nextLine(); // Consume newline character
            String name = scanner.nextLine();
            System.out.print("Enter student date of birth (YYYY-MM-DD): ");
            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
            List<String> classroom = getClassroomFromUser();
            List<String> subject = getSubjectFromUser();
            return (new Student(id, name, dateOfBirth, classroom, subject, LocalDate.now()));
        } catch (DateTimeParseException | InputMismatchException e) {
            System.err.println("Error: Invalid input. Please enter valid data.");
            return null;
        }
    }

    public String getSearchKeywordFromUser(){
        // Prompt user to enter search keyword and return it
        System.out.print("Enter search keyword: ");
        return scanner.next();
    }
    public void notifyError(String message){
        // Display error message to user
        System.err.println("Error: " + message);
    }

    public void displayMenu() {
        String menu = """
            =======================================================================================
            1. ADD NEW STUDENT              2. LIST ALL STUDENTS            3. COMMIT DATA TO FILE
            4. SEARCH FOR STUDENT           5. UPDATE STUDENT'S INFO BY ID  6. DELETE STUDENT'S DATA
            7. GENERATE DATA TO FILE        8. DELETE/CLEAR ALL DATA FROM DATA STORE
            0,99. EXIT
            =======================================================================================
            """;
        System.out.println(menu);
        System.out.print("> Insert option: ");
    }

    public Integer getMenuOptionFromUser(){
        // Prompt user to enter menu option and return it
        return scanner.nextInt();
    }
}
