import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
//        conditionTest();
        readWriteTest();
//        lockTest();
    }

    private static void lockTest() {
        final Lock test = new Lock();

        new Thread("A") {
            @Override
            public void run() {
                test.access();
            }
        }.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread("B") {
            @Override
            public void run() {
                test.access();//A完了B才能进去
            }
        }.start();
    }

    private static void readWriteTest() {
        final ReadWriteLock test = new ReadWriteLock();

        new Thread("A") {
            @Override
            public void run() {
                test.read();
//                test.write();
            }
        }.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread("B") {
            @Override
            public void run() {
//                test.read();//不受lock影响
                test.write();//A释放了锁才能进去lock的部分
            }
        }.start();
    }

    private static void conditionTest() {
        final Conditions conditions = new Conditions();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    conditions.add();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    conditions.get();
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
