package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.Arrays;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L016 {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int total=1000000;
        for(int first=0;first<nums.length;first++){
            if(first>0&&nums[first]==nums[first-1]){
                continue;
            }
            int thrid=nums.length-1;
            int second=first+1;
            while (second<thrid){
                int val=nums[first]+nums[second]+nums[thrid];
                if(target==val){
                    return val;
                }
                int v=Math.abs(val-target);
                if(v<Math.abs(total-target)){
                    total=val;
                }
                if(val>target){
                    int valTmp=nums[thrid];
                    thrid--;
                    while (thrid>second&&valTmp==nums[thrid]){
                        thrid--;
                    }
                }else{
                    int valTmp=nums[second];
                    second++;
                    while (second<nums.length&&valTmp==nums[second]){
                        second++;
                    }
                }

            }
        }
        return total;
    }
}
