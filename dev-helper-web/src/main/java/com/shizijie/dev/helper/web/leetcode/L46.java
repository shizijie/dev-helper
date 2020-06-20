package com.shizijie.dev.helper.web.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shizijie
 * @version 2020-04-19 下午10:38
 */
public class L46 {
    public static void main(String[] args) {
        System.out.println(permute(new int[]{1,2,3}));
    }


    public static List<List<Integer>> permute(int[] arr){
        LinkedList<List<Integer>> ans=new LinkedList<>();
        if(arr.length==0){
            return ans;
        }
        ans.add(Arrays.asList(arr[0]));
        for(int i=1;i<arr.length;i++){
            while (ans.peek().size()==i){
                List<Integer> tmp=ans.remove();
                for(int j=0;j<i+1;j++){
                    List<Integer> s=new ArrayList<>(tmp);
                    s.add(j,arr[i]);
                    ans.add(s);
                }
            }
        }
        return ans;
    }
}
