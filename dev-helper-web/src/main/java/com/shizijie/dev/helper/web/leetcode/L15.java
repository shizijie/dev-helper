package com.shizijie.dev.helper.web.leetcode;

import org.apache.poi.ss.formula.functions.T;

import java.util.*;

public class L15 {
    public static List<List<Integer>> threeSum(int[] nums) {
        if(nums.length<3){
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        Map<String,String> map=new HashMap<>();
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

    public static void main(String[] args) {
        System.out.println(findAnagrams("abcde","cb"));
    }

    public static List<Integer> findAnagrams(String s, String p) {
        int[] cnt = new int[128];
        for (char c : p.toCharArray()) {
            cnt[c]++;
        }
        int lo = 0, hi = 0;
        List<Integer> res = new ArrayList<>();
        //高位小于s长度
        while (hi < s.length()) {
            //当高位字符>0
            if (cnt[s.charAt(hi)] > 0) {
                //高位去除，并自增
                cnt[s.charAt(hi++)]--;
                //如果高位-低位的长度等于p
                if (hi - lo == p.length()) {
                    res.add(lo);
                }
            } else {
                //低位加1，并自增
                cnt[s.charAt(lo++)]++;
            }
        }
        return res;
    }
}
