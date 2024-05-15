import co.istad.mini_project.controller.StudentController;
import co.istad.mini_project.dao.StudentDao;
import co.istad.mini_project.dao.impl.StudentDaoImpl;
import co.istad.mini_project.model.StudentModel;
import co.istad.mini_project.view.StudentView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String PRIMARY_DATA_PATH = "primary_data.txt";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2; // Double the number of available processors
    private static final int BUFFER_SIZE = 1024 * 1024 * 10; // 10 MB buffer size

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.nanoTime();

        // Set up DAO, view, model, and controller
        StudentDao studentDao = new StudentDaoImpl();
        StudentView studentView = new StudentView();
        StudentModel studentModel = new StudentModel();
        StudentController studentController = new StudentController(studentDao, studentView, studentModel);

        // Open the file asynchronously
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get(PRIMARY_DATA_PATH), StandardOpenOption.READ)) {

            // Create thread pool
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

            // Set up latch to wait for all tasks to complete
            CountDownLatch latch = new CountDownLatch(NUM_THREADS);

            // Submit tasks to read portions of the file in parallel
            for (int i = 0; i < NUM_THREADS; i++) {
                long position = i * (fileChannel.size() / NUM_THREADS);
                executor.submit(new FileReader(fileChannel, position, BUFFER_SIZE, latch));
            }

            // Wait for all tasks to complete
            latch.await();

            // Shutdown executor
            executor.shutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        System.out.println("Total execution time for file reading: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

        // Start the application
        studentController.run();
    }

    static class FileReader implements Runnable {
        private final AsynchronousFileChannel fileChannel;
        private final long position;
        private final int bufferSize;
        private final CountDownLatch latch;

        public FileReader(AsynchronousFileChannel fileChannel, long position, int bufferSize, CountDownLatch latch) {
            this.fileChannel = fileChannel;
            this.position = position;
            this.bufferSize = bufferSize;
            this.latch = latch;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            fileChannel.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.flip();
                    // Process the buffer content if needed
                    attachment.clear();
                    latch.countDown();
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    latch.countDown();
                }
            });
        }
    }
}
