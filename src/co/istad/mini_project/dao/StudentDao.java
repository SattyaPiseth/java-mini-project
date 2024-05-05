package co.istad.mini_project.dao;

import co.istad.mini_project.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * @author Sattya
 * create at 5/5/2024 8:55 AM
 */
public interface StudentDao {
    /**
     * Get all students
     * @return list or Retrieve of students from file / database
     */
    List<Student> getAllStudents();

    /**
     * Add student to file / database
     * @param student student to be added
     */
    void addStudent(Student student);

    /**
     * Update student to file / database
     * @param student student to be updated
     */
    void updateStudent(Student student);

    /**
     * Delete student from file / database
     * @param id id of student to be deleted
     */
    void deleteStudent(Integer id);

    /**
     * Search for students by name or ID in file / database
     * @param keyword keyword to be searched
     * @return list or Retrieve of students from file / database
     */
    List<Student> searchStudents(String keyword);

    /**
     * Get student by ID in file / database
     * @param id id of student to be retrieved
     * @return Optional of student from file / database
     */
    Optional<Student> getStudentById(Integer id);
}
