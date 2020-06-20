package com.shizijie.dev.helper.web.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shizijie
 * @version 2020-04-02 下午6:32
 */
public class Test01 {
    ReentrantLock lock=new ReentrantLock();
    Condition aCon=lock.newCondition();
    Condition bCon=lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Test01 test01=new Test01();
        A a=test01.new A();
        B b=test01.new B();
        a.start();
        b.start();
    }

    class A extends Thread{
        @Override
        public void run() {
            while (true){
                try {
                    lock.lock();
                    System.out.print("A");
                    bCon.signal();
                    aCon.await();
                    System.out.println(111);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println(222);
                    lock.unlock();
                }
            }
        }
    }
    class B extends Thread{
        @Override
        public void run() {
            //while (true){
                try {
                    lock.lock();
                    System.out.print("B");
                    aCon.signal();
                    bCon.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            //}
        }
    }
}
