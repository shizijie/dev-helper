package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L001 {
    public int[] twoSum(int[] nums, int target) {
        if(nums==null){
            return null;
        }
        Map<Integer,Integer> map=new HashMap<>();
        for(int i=0;i<nums.length;i++){
            Integer res=map.get(target-nums[i]);
            if(res!=null){
                return new int[]{res,i};
            }else{
                map.put(nums[i],i);
            }
        }
        return null;
    }
}
