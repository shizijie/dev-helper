package com.shizijie.dev.helper.web.leetcode.test2;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L033 {
    public int search(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return -1;
        }
        if (n == 1) {
            return nums[0] == target ? 0 : -1;
        }
        int l = 0, r = n - 1;
        while (l <= r) {
            //去中间段
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            //中间大于等于开始
            if (nums[0] <= nums[mid]) {
                //结果在0到中间值之间
                if (nums[0] <= target && target < nums[mid]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            //中间值小于0
            else {
                //结果在中间值到最后值之间
                if (nums[mid] < target && target <= nums[n - 1]) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
        }
        return -1;
    }
}
