package com.shizijie.dev.helper.web.leetcode.test3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L060 {
    static int[] factorial = new int[10];
    static {
        factorial[0] = 1;
        for (int i = 1; i <= 9; i++) {
            factorial[i] = i * factorial[i - 1];
        }
    }
    public String getPermutation(int n, int k) {
        StringBuilder ans = new StringBuilder();
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        k--;
        for (int i = n - 1; i >= 0; i--) {
            int index = k / factorial[i];
            ans.append(list.remove(index));
            k %= factorial[i];
        }
        return ans.toString();
    }
}
