import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by qiangyang on 2017/7/5.
 */
public class ReentrantReadWriteLockTest {

    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    int a = 0;

    public static void main(String[] args) {
        final ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();

        new Thread("A") {
            @Override
            public void run() {
                test.get(Thread.currentThread());
            }
        }.start();

        new Thread("B") {
            @Override
            public void run() {
                test.get(Thread.currentThread());
            }
        }.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(test.a);
    }

    public void get(Thread thread) {
        rwl.readLock().lock();
        try {
            System.out.println("thread " + thread.getName() + " start read");
            for (int i = 0; i < 100000; i++) {
                a++;
            }
//            while (System.currentTimeMillis() - start <= 30) {
////                System.out.println("thread " + thread.getName() + " reading...");
//                a++;
//            }
            System.out.println("thread " + thread.getName() + " read end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwl.readLock().unlock();
        }
    }

//    public synchronized void get(Thread thread) {
//        long start = System.currentTimeMillis();
//        System.out.println("thread " + thread.getName() + " start read");
//        while (System.currentTimeMillis() - start <= 30) {
//            System.out.println("thread " + thread.getName() + " reading...");
//        }
//        System.out.println("thread " + thread.getName() + " read end");
//    }
}
