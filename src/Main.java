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
    private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 100; // Maximum buffer size (100 MB)
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2; // Utilize available CPU cores efficiently

    public static void main(String[] args) {
        StudentController studentController = new StudentController(new StudentDaoImpl(), new StudentView(), new StudentModel());

        long startTime = System.nanoTime();
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get(PRIMARY_DATA_PATH), StandardOpenOption.READ)) {
            long fileSize = fileChannel.size();
            int bufferSize = calculateBufferSize(fileSize);

            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            CountDownLatch latch = new CountDownLatch((int) Math.ceil((double) fileSize / bufferSize));

            for (long position = 0; position < fileSize; position += bufferSize) {
                executor.submit(new FileReader(fileChannel, position, bufferSize, latch));
            }

            latch.await();
            executor.shutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        System.out.println("Total execution time for file reading: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

        studentController.run();
    }

    private static int calculateBufferSize(long fileSize) {
        return (int) Math.min(MAX_BUFFER_SIZE, fileSize);
    }

    static class FileReader implements Runnable {
        private final AsynchronousFileChannel fileChannel;
        private final long position;
        private final int bufferSize;
        private final CountDownLatch latch;

        FileReader(AsynchronousFileChannel fileChannel, long position, int bufferSize, CountDownLatch latch) {
            this.fileChannel = fileChannel;
            this.position = position;
            this.bufferSize = bufferSize;
            this.latch = latch;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

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
