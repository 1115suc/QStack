package course.QStack.JUCDemo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyRunnableThread implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Runnable");
    }
}

/**
 * 1. 创建Callable的实现类，并重写call()方法，该方法为线程执行体，并且该方法有返回值
 */
class MyCallableThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        
        int i;
        for (i = 0; i < 10; i++) {
            Thread.sleep(300);
            System.out.println(Thread.currentThread().getName() + " 执行了！" + i);
        }

        return i;
    }
}

public class CallableDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 创建多线程
        // new Thread(new MyRunnableThread(), "threadName").start();

        //new Thread(new MyCallableThread(), "threadName").start();
        // 2. 创建Callable的实例，并用FutureTask类来包装Callable对象
        // 3. 创建FutureTask对象，需要一个Callable类型的参数
        FutureTask task = new FutureTask<>(new MyCallableThread());
        // 4. 创建多线程，由于FutureTask的本质是Runnable的实现类，所以第一个参数可以直接使用task
        new Thread(task, "MyCallableThread").start();

        //取消任务
//        Thread.sleep(1000);
//        task.cancel(true); //线程运行时可以被打断吗
//        boolean cancelled = task.isCancelled();
//        System.out.println("cancelled " + cancelled);

        //等待任务执行完毕
//        while (!task.isDone()) { //也可以使用task.isDone()判断子线程是否执行完毕
//            Thread.sleep(100);
//            System.out.println("wait...");
//        }

        //获取结果
        Object o = task.get();
        System.out.println(o);//get方法阻塞主线程，因为需要返回子线程的结果
        System.out.println(Thread.currentThread().getName() + " over!");

    }
}