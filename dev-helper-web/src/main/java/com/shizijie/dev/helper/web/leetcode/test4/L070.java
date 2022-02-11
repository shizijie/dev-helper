package com.shizijie.dev.helper.web.leetcode.test4;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:53
 */
public class L070 {
    public int res=0;

    public int sum=0;

    public int climbStairs(int n) {
        dfs(0,n);
        return res;
    }

    public void dfs(int index,int n){
        if(sum>=n){
            if(sum==n)
                res++;
            return;
        }
        for(int i=1;i<3;i++){
            dfs(i+1,n);
        }
    }
}
