package com.shizijie.dev.helper.web.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2020-04-02 下午5:34
 */
public class Test {
    private static String i;

    public static void main(String[] args) {
        System.out.println(i);
    }

    public static int test1(){
        int tmp=1;
        List<String> list=new ArrayList<>();
        try {
            System.out.println("-----");
            return ++tmp;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ++tmp;
            System.out.println(":"+tmp);
            //return ++tmp;
        }
        return ++tmp;
    }
}
