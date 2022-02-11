package com.shizijie.dev.helper.web.leetcode.test2;

import java.util.*;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L040 {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Stack<Integer> stack=new Stack<>();
        List<List<Integer>> result=new ArrayList<>();
        Arrays.sort(candidates);
        dfs(target,candidates,0,stack,result);
        return result;
    }

    public void dfs(int target,int[] candidates,int index,Stack<Integer> stack,List<List<Integer>> result){
        if(target==0){
            result.add(new ArrayList<>(stack));
            return;
        }
        for(int i=index;i<candidates.length;i++){
            if(i > index && candidates[i] == candidates[i-1]) {
                continue;
            }
            if(candidates[i]<=target){
                stack.push(candidates[i]);
                dfs(target-candidates[i],candidates,i+1,stack,result);
                stack.pop();
            }
        }
    }
}
