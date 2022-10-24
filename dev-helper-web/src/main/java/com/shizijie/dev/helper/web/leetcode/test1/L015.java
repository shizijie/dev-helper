package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 三数之和
 */
public class L015 {
    public List<List<Integer>> threeSum(int[] nums) {
        int length = nums.length;
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int first = 0; first < n; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int thrid = n - 1;
            int target = -nums[first];
            for (int second = first + 1; second < n; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                while (second < thrid && nums[second] + nums[thrid] > target) {
                    thrid--;
                }
                if (second == thrid) {
                    break;
                }
                if (nums[second] + nums[thrid] == target) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[thrid]);
                    res.add(list);
                }
            }
        }
        System.out.println(length);
        return res;
    }
}
