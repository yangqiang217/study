import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by qiangyang on 2017/7/5.
 * 那怎么实现同一段代码可以随便读但是写要加锁？那就包一层read和write方法，把此处sleep 3s改成那个方法
 */
public class ReadWriteLock {

    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    /**
     * 既然读都能随便进来，那加readLock和不加有什么区别：
     * 区别就在于当你要上writelock的时候得等所有readlock退出他才能上。
     */
    public void read() {
        rwl.readLock().lock();
        try {
            System.out.println("read... " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            System.out.println("read finish " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwl.readLock().unlock();
        }
    }

    public void write() {
        rwl.writeLock().lock();

        try {
            System.out.println("write... " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            System.out.println("write finish " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwl.writeLock().unlock();
        }
    }
}
