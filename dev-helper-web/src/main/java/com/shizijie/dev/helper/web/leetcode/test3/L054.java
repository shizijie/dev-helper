package com.shizijie.dev.helper.web.leetcode.test3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L054 {
    private static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int row = matrix.length;
        int col = matrix[0].length;
        int l = 0, t = 0, b = row - 1, r = col - 1;
        while (true) {
            for (int i = l; i <= r; i++) res.add(matrix[t][i]);

            if (++t > b) break;
            for (int i = t; i <= b; i++) res.add(matrix[i][r]);

            if (--r < l) break;
            for (int i = r; i >= l; i--) res.add(matrix[b][i]);

            if (--b < t) break;
            // 此处t已经下挪过一了
            for (int i = b; i >= t; i--) res.add(matrix[i][l]);
            if (++l > r) break;
        }
        return res;
    }
}
