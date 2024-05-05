package co.istad.mini_project.controller;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;
import co.istad.mini_project.view.StudentView;

import java.util.List;
import java.util.Optional;

/**
 * @author Sattya
 * create at 5/5/2024 9:07 AM
 */
public class StudentController {
    private final StudentDao studentDao;
    private final StudentView studentView;

    // Constructor
    public StudentController(StudentDao studentDao, StudentView studentView) {
        this.studentDao = studentDao;
        this.studentView = studentView;
    }
    //TODO: Implement the methods of the controller
    public void displayStudents(){
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

}
