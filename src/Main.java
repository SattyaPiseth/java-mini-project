import co.istad.mini_project.controller.StudentController;
import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.dao.impl.StudentDaoImpl;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String PRIMARY_DATA_PATH = "primary_data.txt";

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        // Set up DAO, view, model, and controller
        StudentDao studentDao = new StudentDaoImpl();
        StudentView studentView = new StudentView();
        StudentModel studentModel = new StudentModel();
        StudentController studentController = new StudentController(studentDao, studentView, studentModel);

        // Read the file and start the application
        try (var reader = Files.newBufferedReader(Paths.get(PRIMARY_DATA_PATH))) {
            int parallelismLevel = Runtime.getRuntime().availableProcessors();
            ForkJoinPool customThreadPool = new ForkJoinPool(parallelismLevel); // Adjust the parallelism level as needed
            try {
                customThreadPool.submit(() ->
                        // Process each line as needed
                        reader.lines()
                                .parallel() // Enable parallel processing
                                .forEach(System.out::println)
                ).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                customThreadPool.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        System.out.println("Total execution time: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

        // Start the application
        studentController.run();
    }
}
