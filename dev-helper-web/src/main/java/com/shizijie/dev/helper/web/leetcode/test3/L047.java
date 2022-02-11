package com.shizijie.dev.helper.web.leetcode.test3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L047 {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res=new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(nums,0,new Stack<>(),res,visited);
        return res;
    }
    public void dfs(int[] nums, int index, Stack<Integer> stack,List<List<Integer>> res,boolean[] visited){
        if(index==nums.length){
            res.add(new ArrayList<>(stack));
            return;
        }
        for(int i=0;i<nums.length;i++){
            if(!visited[i]){
                if(i>0&&nums[i]==nums[i-1]&& !visited[i - 1]){
                    continue;
                }
                stack.push(nums[i]);
                visited[i]=true;
                dfs(nums,index+1,stack,res,visited);
                stack.pop();
                visited[i]=false;
            }
        }
    }
}
