package com.shizijie.dev.helper.web.leetcode.test3;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L055 {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        int rightmost = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= rightmost) {
                rightmost = Math.max(rightmost, i + nums[i]);
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJump2(int[] nums) {
        int n = nums.length;

        int far = 0;
        for (int i = 0; i < n && far < n - 1; i++) {
            far = Math.max(far, i + nums[i]);
            if (far == i) break;
        }

        return far >= n - 1;
    }
}
