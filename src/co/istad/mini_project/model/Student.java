package co.istad.mini_project.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sattya
 * create at 5/5/2024 8:46 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> classroom;
    private List<String> subject;
    private LocalDate date;
}
