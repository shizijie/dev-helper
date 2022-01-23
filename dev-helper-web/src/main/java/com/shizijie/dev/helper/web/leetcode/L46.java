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
//    public static void main(String[] args) {
//        System.out.println(permute(new int[]{1,2,3}));
//    }


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

    public int lengthOfLongestSubstring(String s) {
        if(s==null){
            return 0;
        }
        if(s.length()<=1){
            return s.length();
        }
        int total=1;
        for(int start=0,end=1;end<s.length();end++){
            char a=s.charAt(end);
            int index=s.indexOf(a,start);
            if(index<end){
                start=index+1;
            }
            int length=end-start+1;
            if(length>total){
                total=length;
            }
        }
        return total;
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("abacab"));
    }

    public static String longestPalindrome(String s) {
        if(s.length()<=1){
            return s;
        }
        char[] arr=s.toCharArray();
        String res=s.substring(0,1);

        for(int i=0;i<arr.length-1;i++){
            int j=arr.length-1;
            boolean isOk=true;
            int start=i;
            int end=j;
            while (i<j){
                if(arr[i]!=arr[j]){
                    i=start;
                    j=end-1;
                    end=j;
                    continue;
                }else{
                    i++;
                    j--;
                }
            }
            if(isOk&&end>start){
                String str=s.substring(start,end+1);
                if(str.length()>res.length()){
                    res=str;
                }
            }
            i=start;
        }
        return res;

    }
}
