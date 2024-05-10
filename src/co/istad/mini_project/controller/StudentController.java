package co.istad.mini_project.controller;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class StudentController {
    private final StudentDao studentDao;
    private final StudentView studentView;
    private final StudentModel studentModel;

    public StudentController(StudentDao studentDao, StudentView studentView, StudentModel studentModel) {
        this.studentDao = studentDao;
        this.studentView = studentView;
        this.studentModel = studentModel;
    }

    public void run() {
        while (true) {
            studentView.displayMenu();
            int choice = studentView.getMenuOptionFromUser();
            processUserChoice(choice);
        }
    }

    private void processUserChoice(int choice) {
        switch (choice) {
            // add student
            case 1:
                addStudent();
                break;
            // list all students
            case 2:
                displayStudents();
                break;
            // commit data to file
            case 3:

                break;
            // search for student
            case 4:
                searchStudents();
                break;
            // update student's info by id
            case 5:
                updateStudent();
                break;
            // delete student's data
            case 6:
                deleteStudent();
                break;

            // generate data to file
            case 7:
                break;

            // delete/clear all data from data store
            case 8:
                break;

            // exit
            case 0, 99:
                exit();
                break;

            default:
                studentView.notifyError("Invalid choice. Please try again.");
        }
    }

    private void displayStudents() {
        try {
            List<Student> students = studentDao.getAllStudents();
            studentModel.setStudents(students);
            displayFirstPage();
        } catch (FileNotFoundException e) {
            studentView.notifyError("File not found: " + e.getMessage());
        }
    }

    private void addStudent() {
        Student student = studentView.getStudentInfoFromUser();
        studentDao.addStudent(student);
    }

    private void updateStudent() {
        Integer id = studentView.getStudentIdFromUser();
        Optional<Student> studentOptional = studentDao.getStudentById(id);
        studentOptional.ifPresentOrElse(student -> {
            Student updatedStudent = studentView.updateStudentInfoFromUser(student.getId());
            updatedStudent.setId(student.getId());
            updatedStudent.setDate(student.getDate()); // Preserve original date
            studentDao.updateStudent(updatedStudent);
        }, () -> studentView.notifyError("Student not found with ID: " + id));
    }

    private void deleteStudent() {
        Integer id = studentView.getStudentIdFromUser();
        studentDao.deleteStudent(id);
    }

    private void searchStudents() {
        String keyword = studentView.getSearchKeywordFromUser();
        List<Student> students = studentDao.searchStudents(keyword);
        studentView.displayStudents(students);
    }

    private void displayCurrentPage() {
        studentView.displayStudents(studentModel.getCurrentPageStudents());
        studentView.displayPageInfo(studentModel.getCurrentPage(), studentModel.getTotalPages());
        handlePaginationOption();
    }
    private void displayFirstPage(){
        studentModel.setCurrentPage(1);
        displayCurrentPage();
    }

    private void handlePaginationOption() {
        System.out.println("1. Next page");
        System.out.println("2. Previous page");
        System.out.println("3. Set page size");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        int choice = studentView.getMenuOptionFromUser();
        switch (choice) {
            case 1:
                nextPage();
                break;
            case 2:
                previousPage();
                break;
            case 3:
                System.out.print("Enter new page size: ");
                setPageSize(studentView.getMenuOptionFromUser());
                displayCurrentPage();
                break;
            case 4:
                break;
            default:
                studentView.notifyError("Invalid choice. Please try again.");
        }
    }

    private void nextPage() {
        if (studentModel.getCurrentPage() < studentModel.getTotalPages()) {
            studentModel.setCurrentPage(studentModel.getCurrentPage() + 1);
            displayCurrentPage();
        } else {
            System.out.println("Already on the last page");
        }
    }

    private void previousPage() {
        if (studentModel.getCurrentPage() > 1) {
            studentModel.setCurrentPage(studentModel.getCurrentPage() - 1);
            displayCurrentPage();
        } else {
            System.out.println("Already on the first page");
        }
    }

    private void setPageSize(int pageSize) {
        studentModel.setPageSize(pageSize);
    }

    private void exit() {
        System.exit(0);
    }
}
