package com.study.thread;

/**
 * http://blog.csdn.net/lufeng20/article/details/24314381
 * @author v-yangqiang
 *
 */
public class ThreadLocalTest {

    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>();
    
    private static class TestClient extends Thread {
        private int a;
        public TestClient(int a) {
            this.a = a;
        }
        
        @Override
        public void run() {
            seqNum.set(a);
            System.out.println("thread: " + Thread.currentThread().getName() + ", seq: " + seqNum.get());
        }
    }
    
    public static void main(String[] args) {
        ThreadLocalTest tlt = new ThreadLocalTest();
        TestClient t1 = new TestClient(1);
        TestClient t2 = new TestClient(2);
        TestClient t3 = new TestClient(3);
        t1.start();
        t2.start();
        t3.start();
    }

}
