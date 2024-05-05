import co.istad.mini_project.controller.StudentController;
import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.dao.impl.StudentDaoImpl;
import co.istad.mini_project.view.StudentView;

public class Main {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDaoImpl();
        StudentView studentView = new StudentView();
        StudentController studentController = new StudentController(studentDao, studentView);

        // Application starts here
        boolean exit = false;
        while (!exit) {
            // Display menu options
            // Prompt user for choice and call corresponding controller method
        }
    }
}