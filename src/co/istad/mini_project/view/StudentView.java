package co.istad.mini_project.view;

import co.istad.mini_project.model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Sattya
 * create at 5/5/2024 9:00 AM
 */
public class StudentView {
    private Scanner scanner;

    // Constructor
    public StudentView(){
        scanner = new Scanner(System.in);
    }

    public void displayStudents(List<Student> students){
        // Display all students
        students.stream()
                .sorted(Comparator.comparing(Student::getId))
                .toList()
                .forEach(System.out::println);
    }
    public void displayPageInfo(Integer currentPage, Integer totalPages){
        // Display page info
        System.out.println("Page " + currentPage + " of " + totalPages);
    }
    public Student getStudentInfoFromUser() {
        // Prompt user to enter student information and return a new Student object
        System.out.print("Enter student ID: ");
        Integer id = scanner.nextInt();
        System.out.print("Enter student name: ");
        String name = scanner.next();
        System.out.print("Enter student date of birth (yyyy-MM-dd): ");
        String dateOfBirth = scanner.next();
        System.out.print("Enter student classroom: ");
        String classroom = scanner.next();
        System.out.print("Enter student subject: ");
        String subject = scanner.next();
        return new Student(id, name, LocalDate.parse(dateOfBirth.formatted(DateTimeFormatter.ofPattern(
                                 "yyyy-MM-dd"
        ))), classroom, subject, LocalDate.now());
    }
    public Integer getStudentIdFromUser(){
        // Prompt user to enter student ID and return it
        return 0;
    }
    public String getSearchKeywordFromUser(){
        // Prompt user to enter search keyword and return it
        return null;
    }
    public void notifyError(String message){
        // Display error message to user
    }

    public void displayMenu(){
        // Display the main menu
        System.out.println("1. Display students");
        System.out.println("2. Add student");
        System.out.println("3. Update student");
        System.out.println("4. Delete student");
        System.out.println("5. Search students");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }
    public Integer getMenuOptionFromUser(){
        // Prompt user to enter menu option and return it
        return scanner.nextInt();
    }
}
