import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by qiangyang on 2017/7/5.
 */
public class 验证两个方法都有lock是不是同一时间只有一个能执行 {

    public static void main(String[] args) {
        Locked locked = new Locked();
        new Thread() {
            @Override
            public void run() {
                locked.func1();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                locked.func2();
            }
        }.start();
    }
}

class Locked {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void func1() {
        lock.readLock().lock();
        try {
            while (true) {
                System.out.println("func1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void func2() {
        lock.writeLock().lock();
        try {
            while (true) {
                System.out.println("func2-------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
