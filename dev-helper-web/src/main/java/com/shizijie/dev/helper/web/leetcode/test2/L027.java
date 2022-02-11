package com.shizijie.dev.helper.web.leetcode.test2;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L027 {
    public int removeElement(int[] nums, int val) {
        if(nums.length==0){
            return 0;
        }
        int oldIndex=0;
        int newIndex=0;
        for(;newIndex<nums.length;newIndex++){
            if(nums[newIndex]!=val){
                nums[oldIndex]=nums[newIndex];
                oldIndex++;
            }
        }
        return oldIndex;
    }
}
