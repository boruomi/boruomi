package thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VirtualThreadTest {
    @Test
    public void test1() {
        // 创建一个虚拟线程并启动
        Runnable task1 = () -> {
            System.out.println("Hello from virtual thread: " + Thread.currentThread());
        };

        Thread virtualThread = Thread.ofVirtual().start(task1);

        try {
            virtualThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        // 使用虚拟线程池
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Runnable task2 = () -> {
                System.out.println("Hello from virtual thread pool: " + Thread.currentThread());
            };

            for (int i = 0; i < 10; i++) {
                executor.submit(task2);
            }
        }
    }
}
