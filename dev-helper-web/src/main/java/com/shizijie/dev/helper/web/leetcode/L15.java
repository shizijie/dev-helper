package com.shizijie.dev.helper.web.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class L15 {
    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{3,0,-2,-1,1,2}));
    }
    public static List<List<Integer>> threeSum(int[] nums) {
        if(nums.length<3){
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        List<List<Integer>> list=new ArrayList<>();
        for(int i=0;nums[i]<=0&&i<nums.length-2;i++){
            if(nums[i]>0||nums[nums.length-1]<0){
                break;
            }
            if(i>0&&nums[i]==nums[i-1]){
                continue;
            }
            int a=i+1;
            int third = nums.length - 1;
            while(a<nums.length-1){
                if(a>i+1&&nums[a]==nums[a-1]){

                }else{
                    while (a<third&&nums[a]+nums[third]+nums[i]>0){
                        third--;
                    }
                    if(a==third){
                        break;
                    }
                    if(nums[a]+nums[third]+nums[i]==0){
                        List<Integer> tmp=new ArrayList<>();
                        tmp.add(nums[i]);
                        tmp.add(nums[a]);
                        tmp.add(nums[third]);
                        list.add(tmp);

                    }
                }
                a++;
            }

        }
        return list;
    }
}
