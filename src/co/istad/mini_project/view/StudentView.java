package co.istad.mini_project.view;

import co.istad.mini_project.model.Student;

import java.util.List;
import java.util.Scanner;

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
        // Display or list of students
    }
    public Student getStudentInfoFromUser(){
        // Prompt user to enter student information and return a new Student object
        return null;
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
}
