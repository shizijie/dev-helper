package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 转换
 */
public class L007 {
    public int reverse(int x) {
        int inch = x;
        if (x == 0) {
            return x;
        }
        int top = 0;
        int res = 0;
        while (x != 0 || top != 0) {
            int val = x % 10 + top;
            x = x / 10;
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && val > 7)) {
                return 0;
            } else if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && val < -8)) {
                return 0;
            }
            res = res * 10 + val;
            top = top / 10;
        }
        System.out.println(inch);
        return res;
    }
}
