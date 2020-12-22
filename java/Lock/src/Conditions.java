import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.Condition中的await()方法相当于Object的wait()方法，Condition中的signal()方法
 * 相当于Object的notify()方法，Condition中的signalAll()相当于Object的notifyAll()
 * 方法。不同的是，Object中的这些方法是和同步锁捆绑使用的；而Condition是需要与
 * 互斥锁/共享锁捆绑使用的。
 *
 * 2.Condition它更强大的地方在于：能够更加精细的控制多线程的休眠与唤醒。对于同一个锁，
 * 我们可以创建多个Condition，在不同的情况下使用不同的Condition。
 * 例如，假如多线程读/写同一个缓冲区：当向缓冲区中写入数据之后，唤醒"读线程"；当从缓冲
 * 区读出数据之后，唤醒"写线程"；并且当缓冲区满的时候，"写线程"需要等待；当缓冲区为空
 * 时，"读线程"需要等待。
 *
 * 如果采用Object类中的wait(), notify(), notifyAll()实现该缓冲区，当向缓冲区写入数
 * 据之后需要唤醒"读线程"时，不可能通过notify()或notifyAll()明确的指定唤醒"读线程"，
 * 而只能通过notifyAll唤醒所有线程(但是notifyAll无法区分唤醒的线程是读线程，还是写线
 * 程)。  但是，通过Condition，就能明确的指定唤醒读线程。
 */
public class Conditions {

    private final Lock lock = new ReentrantLock();
    private final Condition addCondition = lock.newCondition();
    private final Condition getCondition = lock.newCondition();

    private List<String> lists = new LinkedList<>();

    public void add() {
        lock.lock();

        try {
            while (lists.size() == 3) {
                System.out.println("add wait");
                addCondition.await();
            }
            System.out.println("add...");
            lists.add("shit");
            getCondition.signal();//通知可以get了
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        lock.lock();

        try {
            while (lists.size() == 0) {
                System.out.println("get wait");
                getCondition.await();
            }
            System.out.println("get...");

            String res = lists.remove(0);
            addCondition.signal();//通知可以add了

            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
