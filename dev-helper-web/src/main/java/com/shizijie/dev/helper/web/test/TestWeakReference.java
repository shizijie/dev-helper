package com.shizijie.dev.helper.web.test;

import java.lang.ref.WeakReference;

/**
 * @author shizijie
 * @version 2020-05-27 下午3:10
 */
public class TestWeakReference {
    public static void main(String[] args) {

        Car car = new Car(22000,"silver");
        WeakReference<Car> weakCar = new WeakReference<>(car);

        int i=0;
        boolean gc=true;
        while(true){
            System.out.println("here is the strong reference 'car' "+car);
            if(weakCar.get()!=null){
                i++;
                System.out.println("Object is alive for "+i+" loops - "+weakCar);
            }else{
                System.out.println("Object has been collected.");
                break;
            }
            if(i>500000&&gc){
                System.out.println("==============================================");
                Runtime.getRuntime().gc();
                gc=!gc;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(weakCar.get());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
