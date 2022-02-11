package com.shizijie.dev.helper.web.leetcode.test2;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L026 {
    public int removeDuplicates(int[] nums) {
        if(nums.length==0){
            return 0;
        }
        int fast=1,slow=1;
        for(;fast<nums.length;fast++){
            if(nums[fast]!=nums[fast-1]){
                nums[slow]=nums[fast];
                slow++;
            }
        }
        return slow;
    }
}
