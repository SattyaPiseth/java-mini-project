package co.istad.mini_project.controller;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.dao.impl.StudentDaoImpl;
import co.istad.mini_project.model.Student;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
        try {
            promptCommitData();
            while (true) {
                studentView.displayMenu();
                int option = studentView.getMenuOptionFromUser();
                processUserChoice(option);
            }
        } catch (IOException e) {
            studentView.notifyError("An error occurred: " + e.getMessage());
        }
    }
    private void promptCommitData() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to commit data before starting? (Y/N): ");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("Y")) {
            commitDataToFile();
        } else {
            StudentDaoImpl.clearTransactionFiles();
        }
    }

    private void processUserChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayStudents();
                case 3 -> commitDataToFile();
                case 4 -> searchStudents();
                case 5 -> updateStudent();
                case 6 -> deleteStudent();
                case 7 -> generateDataToFile();
                case 8 -> deleteAllData();
                case 0, 99 -> exit();
                default -> studentView.notifyError("Invalid choice. Please try again.");
            }
        } catch (IOException e) {
            studentView.notifyError("An error occurred: " + e.getMessage());
        }
    }
    private void generateDataToFile() {
        System.out.print("Enter number of records to generate: "); 
        int numberOfRecords = studentView.getMenuOptionFromUser();
        studentDao.generateDataToFile(numberOfRecords);
    }

    private void deleteAllData() {
        studentDao.deleteAllData();
    }


    private void displayStudents() {
        try {
            List<Student> students = studentDao.getAllStudents();
            studentModel.setStudents(students);
            displayFirstPage();
        } catch (IOException e) {
            studentView.notifyError("An error occurred while fetching students: " + e.getMessage());
        }
    }

    private void addStudent() throws IOException {
        Student student = studentView.getStudentInfoFromUser();
        studentDao.addStudent(student);
    }

    private void updateStudent() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student id: ");
        Integer id = scanner.nextInt();
        Optional<Student> studentOptional = studentDao.getStudentById(id);
        studentOptional.ifPresentOrElse(
                student -> {
                    Student newStudent = studentView.updateStudentInfoFromUser(student.getId());
                    studentDao.updateStudent(newStudent);
                },
                () -> studentView.notifyError("Student not found with id: " + id)
        );
    }


    private void deleteStudent() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student id: ");
        Integer id = scanner.nextInt();
        Optional<Student> studentOptional = studentDao.getStudentById(id);
        studentOptional.ifPresentOrElse(
                student -> {
                    try {
                        studentDao.deleteStudent(student.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> studentView.notifyError("Student not found with id: " + id)
        );
    }

    private void searchStudents() throws IOException {
        String keyword = studentView.getSearchKeywordFromUser();
        List<Student> students = studentDao.searchStudents(keyword);
        studentView.displayStudents(students);
    }


    private void displayCurrentPage() {
//        studentView.displayStudents(studentModel.getCurrentPageStudents());
//        studentView.displayPageInfo(studentModel.getCurrentPage(), studentModel.getTotalPages());
//        handlePaginationOption();
        try {
            List<Student> currentPageStudents = studentModel.getCurrentPageStudents();
            studentView.displayStudents(currentPageStudents);
            int currentPage = studentModel.getCurrentPage();
            int totalPages = studentModel.getTotalPages();
            studentView.displayPageInfo(currentPage, totalPages);
            handlePaginationOption();
        } catch (Exception e) {
            studentView.notifyError("An error occurred while displaying the current page: " + e.getMessage());
        }
    }
    private void displayFirstPage() {
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
            case 1 -> nextPage();
            case 2 -> previousPage();
            case 3 -> {
                System.out.print("Enter new page size: ");
                setPageSize(studentView.getMenuOptionFromUser());
                displayCurrentPage();
            }
            case 4 -> { /* do nothing */ }
            default -> studentView.notifyError("Invalid choice. Please try again.");
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

    private void commitDataToFile() {
        studentDao.commit();
    }
}
