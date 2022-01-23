package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L008 {
    public static void main(String[] args) {
        L008 l = new L008();
        System.out.println(l.myAtoi("4193 with words"));
    }

    public int myAtoi(String s) {
        s = s.trim();
        int fuhao = 1;
        int total = 0;
        Boolean start = null;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                if (s.charAt(i) == '-') {
                    fuhao = -1;
                    start = true;
                    continue;
                } else if (s.charAt(i) == '+') {
                    start = true;
                    continue;
                }
            }
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                if (start == null) {
                    start = true;
                }
                int tmp = s.charAt(i) - '0';
                if (fuhao == 1 && (total > Integer.MAX_VALUE / 10 || (total == Integer.MAX_VALUE/10 && tmp > 7))) {
                    return Integer.MAX_VALUE;
                } else if ((fuhao == -1) && (-1 * total < Integer.MIN_VALUE / 10 || (-1 * total == Integer.MIN_VALUE / 10 && -1 * tmp < -8))) {
                    return Integer.MIN_VALUE;
                }
                total = total * 10 + tmp;
            } else if (start == null) {
                return 0;
            } else if (start) {
                break;
            }
        }
        return total * fuhao;
    }
}
