package co.istad.mini_project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sattya
 * create at 5/5/2024 11:16 AM
 */
@Getter
@Setter
@ToString
public class StudentModel {
    private List<Student> students;
    private Integer pageSize;
    private Integer currentPage;



    public StudentModel(){
        students = new ArrayList<>();
        pageSize = 10;
        currentPage = 1;
    }

    public List<Student> getCurrentPageStudents(){
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, students.size());
        return students.subList(startIndex, endIndex);
    }
    public int getTotalPages() {
        return (int) Math.ceil((double) students.size() / pageSize);
    }
}
