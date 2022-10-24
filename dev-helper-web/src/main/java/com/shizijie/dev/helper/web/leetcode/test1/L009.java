package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 是否回文
 */
public class L009 {
    public static void main(String[] args) {
        L009 l = new L009();
        System.out.println(l.isPalindrome(121));
    }

    public boolean isPalindrome(int x) {
        int print = x;
        if (x == 0) {
            return true;
        }
        if (x < 0) {
            return false;
        }
        int tmp = x;
        int total = 0;
        while (x > 0) {
            int val1 = x % 10;
            total = total * 10 + val1;
            x = x / 10;
        }
        System.out.println(print);
        return tmp == total;
    }
}
