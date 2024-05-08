package co.istad.mini_project.controller;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author Sattya
 * create at 5/5/2024 9:07 AM
 */
public class StudentController {
    private final StudentDao studentDao;
    private final StudentView studentView;
    private final StudentModel studentModel;

    // Constructor
    public StudentController(StudentDao studentDao, StudentView studentView, StudentModel studentModel) {
        this.studentDao = studentDao;
        this.studentView = studentView;
        this.studentModel = studentModel;
    }

    //TODO: Implement the methods of the controller
    public void displayStudents() throws FileNotFoundException {
        List<Student> students = studentDao.getAllStudents();

        studentView.displayStudents(students);
    }

    public void addStudent(){
        Student student = studentView.getStudentInfoFromUser();
                studentDao.addStudent(student);
    }

    public void updateStudent(){
        Integer id = studentView.getStudentIdFromUser();
        Optional<Student> student = Optional.of(studentDao.getStudentById(id).orElseThrow());
        // Update the student details
        studentDao.updateStudent(student.get());
    }

    public void deleteStudent(){
        Integer id = studentView.getStudentIdFromUser();
        studentDao.deleteStudent(id);
    }

    public void searchStudents(){
        String keyword = studentView.getSearchKeywordFromUser();
        List<Student> students = studentDao.searchStudents(keyword);
        studentView.displayStudents(students);
    }

    public void setStudents(List<Student> students) {
        studentModel.setStudents(students);
    }

    public void setPageSize(int pageSize) {
        studentModel.setPageSize(pageSize);
    }

    public void setCurrentPage(int currentPage) {
        studentModel.setCurrentPage(currentPage);
    }

    public void nextPage() {
        if (studentModel.getCurrentPage() < studentModel.getTotalPages()) {
            studentModel.setCurrentPage(studentModel.getCurrentPage() + 1);
            displayCurrentPage();
        } else {
            System.out.println("Already on the last page");
        }
    }

    public void previousPage() {
        if (studentModel.getCurrentPage() > 1) {
            studentModel.setCurrentPage(studentModel.getCurrentPage() - 1);
            displayCurrentPage();
        } else {
            System.out.println("Already on the first page");
        }
    }

    public void displayCurrentPage() {
        studentView.displayStudents(studentModel.getCurrentPageStudents());
        studentView.displayPageInfo(studentModel.getCurrentPage(), studentModel.getTotalPages());

        // print option pagination next previous for user choose and set page and size of page
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
}
