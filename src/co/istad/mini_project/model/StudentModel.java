package co.istad.mini_project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StudentModel {
    private List<Student> students;
    private int pageSize;
    private int currentPage;

    public StudentModel() {
        students = new ArrayList<>();
        pageSize = 100; // Adjust page size as needed
        currentPage = 1;
    }

    public List<Student> getCurrentPageStudents() {
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, students.size());
        return new ArrayList<>(students.subList(startIndex, endIndex));
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) students.size() / pageSize);
    }

    // Load data lazily in smaller chunks
    public void loadMoreData(List<Student> additionalStudents) {
        students.addAll(additionalStudents);
    }
}
