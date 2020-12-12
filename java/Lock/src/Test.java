import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qiangyang on 2017/7/5.
 */
public class Test {

    private ArrayList<Integer> arrayList = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final Test test = new Test();

        new Thread("A") {
            @Override
            public void run() {
                try {
                    test.insert(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Thread t = new Thread("B") {
            @Override
            public void run() {
                try {
                    test.insert(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }

    public void insert(Thread thread) throws InterruptedException {
        if (lock.tryLock(3, TimeUnit.SECONDS)) {
            try {
                System.out.println("thread " + thread.getName() + " got lock");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(i);
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("thread " + thread.getName() + " release lock");
                lock.unlock();
            }
        } else {
            System.out.println("thread " + thread.getName() + " got lock fail");
        }
    }
}
