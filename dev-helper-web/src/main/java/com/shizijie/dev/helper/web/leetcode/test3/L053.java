package com.shizijie.dev.helper.web.leetcode.test3;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L053 {
    public int maxSubAray(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int x : nums) {
            //pre来维护对于当前f(i)的f(i−1)的值是多少
            pre = Math.max(pre + x, x);//判断f(i-1)是否要加到当前数上
            maxAns = Math.max(maxAns, pre);//获取最大值
        }
        return maxAns;
    }
}
