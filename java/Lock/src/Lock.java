import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by qiangyang on 2017/7/5.
 */
public class Lock {

    final ReentrantLock lock = new ReentrantLock();

    public void access() {
        lock.lock();
        try {
            System.out.println("access... " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            System.out.println("access finish " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
