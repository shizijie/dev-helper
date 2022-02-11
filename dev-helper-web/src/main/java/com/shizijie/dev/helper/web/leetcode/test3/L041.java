package com.shizijie.dev.helper.web.leetcode.test3;

import java.util.*;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L041 {
    public int firstMissingPositive(int[] nums) {
        Arrays.sort(nums);
        Integer total=null;
        Map<Integer,Integer> map=new HashMap<>(nums.length);
        for(int i=0;i<nums.length;i++){
            if(nums[i]>0&&total==null){
                total=i;
            }
            if(total!=null){
                map.put(nums[i],i);
            }
        }
        if(total==null){
            return 1;
        }
        int max=nums[nums.length-1];
        for(int i=1;i<=max;i++){
            if(map.get(i)==null){
                return i;
            }
        }
        return max+1;
    }
}
