package course.QStack.JUCDemo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        // 创建计数器
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "执行完毕");
                countDownLatch.countDown();
            }, "线程" + i).start();
        }

        try {
            // 等待所有线程执行完毕
            countDownLatch.await();
            System.out.println("所有线程执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
