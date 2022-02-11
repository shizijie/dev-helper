package com.shizijie.dev.helper.web.leetcode.test3;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L048 {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n-1-i; j++) {
                int temp1 = matrix[i][j];
                int temp2 = matrix[j][n-1-i];
                int temp3 = matrix[n-1-i][n-1-j];
                matrix[i][j] = matrix[n-1-j][i];
                matrix[j][n-1-i] = temp1;
                matrix[n-1-i][n-1-j] = temp2;
                matrix[n-1-j][i] = temp3;
            }
        }
    }
}
