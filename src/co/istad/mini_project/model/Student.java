package co.istad.mini_project.model;

import lombok.*;

import java.time.LocalDate;

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
    private String classroom;
    private String subject;
    private LocalDate date;
}
