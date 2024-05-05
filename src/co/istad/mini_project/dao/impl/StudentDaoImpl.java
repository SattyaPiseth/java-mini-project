package co.istad.mini_project.dao.impl;

import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * @author Sattya
 * create at 5/5/2024 8:59 AM
 */
public class StudentDaoImpl implements StudentDao {
    @Override
    public List<Student> getAllStudents() {
        return null;
    }

    @Override
    public void addStudent(Student student) {

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
