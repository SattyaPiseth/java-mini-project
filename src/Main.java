import co.istad.mini_project.controller.StudentController;
import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.dao.impl.StudentDaoImpl;
import co.istad.mini_project.model.Student;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        StudentDao studentDao = new StudentDaoImpl();
        StudentView studentView = new StudentView();
        StudentModel studentModel = new StudentModel();
        StudentController studentController = new StudentController(studentDao, studentView,studentModel);

//        // Sample data
//        List<Student> students = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            students.add( i, new Student(i+1, "Student " + i, "2000-01-23", "Data","Dev",LocalDate.now()));
//        }

//        // set data and page size
//        studentController.setStudents(students);
//        studentController.setPageSize(10);
//
//        // display initial page
//        studentController.displayCurrentPage();


        // Application starts here
        boolean exit = false;
        while (!exit) {
            // Display menu options
            // Prompt user for choice and call corresponding controller method
            // Exit loop when user chooses to exit

            studentView.displayMenu();
            int choice = studentView.getMenuOptionFromUser();
            switch (choice) {
                case 1:
                    studentController.setStudents(studentDao.getAllStudents());
                    studentController.setPageSize(10);
                    studentController.displayCurrentPage();

                    break;
                case 2:
                    studentController.addStudent();
                    break;
                case 3:
                    studentController.updateStudent();
                    break;
                case 4:
                    studentController.deleteStudent();
                    break;
                case 5:
                    studentController.searchStudents();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    studentView.notifyError("Invalid choice. Please try again.");
            }
        }
    }
}