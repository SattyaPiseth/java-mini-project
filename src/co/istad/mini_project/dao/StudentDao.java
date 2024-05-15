package co.istad.mini_project.dao;

import co.istad.mini_project.model.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    List<Student> getAllStudents() throws FileNotFoundException;

    /**
     * Add student to file / database
     * @param student student to be added
     */
    void addStudent(Student student);

    /**
     * Update student to file / database
     * @param  student to be updated
     */
    void updateStudent(Student student);

    /**
     * Delete student from file / database
     * @param id id of student to be deleted
     */
    void deleteStudent(Integer id) throws IOException;

    /**
     * Search for students by name or ID in file / database
     * @param keyword keyword to be searched
     * @return list or Retrieve of students from file / database
     */
    List<Student> searchStudents(String keyword) throws IOException;

    /**
     * Get student by ID in file / database
     * @param id id of student to be retrieved
     * @return Optional of student from file / database
     */
    Optional<Student> getStudentById(Integer id) throws IOException;

    /**
     *  Commit transaction to file / database
     */
    void commit();

    /**
     * Delete all data from file / database
     */
    void deleteAllData();

    void generateDataToFile(int numRecords);
}
