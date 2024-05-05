package co.istad.mini_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Sattya
 * create at 5/5/2024 8:46 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String dateOfBirth;
    private String classroom;
    private String subject;
    private LocalDate date;
}
