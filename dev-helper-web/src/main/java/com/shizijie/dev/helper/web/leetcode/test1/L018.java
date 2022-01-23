package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L018 {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> res=new ArrayList<>();
        for(int first=0;first<nums.length;first++){
            if(first>0&&nums[first]==nums[first-1]){
                continue;
            }
            for(int second=first+1;second<nums.length;second++){
                if(second>first+1&&nums[second]==nums[second-1]){
                    continue;
                }
                int thrid=second+1;
                int forth=nums.length-1;
                while (thrid<forth){
                    if(thrid>second+1&&nums[thrid]==nums[thrid-1]){
                        thrid++;
                        continue;
                    }else if(forth<nums.length-1&&nums[forth]==nums[forth+1]){
                        forth--;
                        continue;
                    }
                    int sum=nums[first]+nums[second]+nums[thrid]+nums[forth];
                    if(target==sum){
                        List<Integer> tmp=new ArrayList<>();
                        tmp.add(nums[first]);
                        tmp.add(nums[second]);
                        tmp.add(nums[thrid]);
                        tmp.add(nums[forth]);
                        res.add(tmp);
                        thrid++;
                    }else if(target>sum){
                        thrid++;
                    }else if(target<sum){
                        forth--;
                    }
                }
            }
        }
        return res;
    }
}
