package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 最大面积
 */
public class L011 {
    public int maxArea(int[] height) {
        int valve = height.length;
        int total = 0;
        int start = 0;
        int end = height.length - 1;
        while (end > start) {
            total = Math.max(total, (end - start) * Math.min(height[start], height[end]));
            if (height[start] <= height[end]) {
                start++;
            } else {
                end--;
            }
        }
        System.out.println(valve);
        return total;
    }
}
