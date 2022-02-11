package com.shizijie.dev.helper.web.leetcode.test2;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L032 {
    public int longestValidParentheses(String s) {
        if(s==null||s.length()<=1){
            return 0;
        }
        return getLen(s);
    }

    public int getLen(String s){
        int left=0;
        int right=0;
        int total=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='('){
                left++;
            }else if(s.charAt(i)==')'){
                right++;
            }
            if(left==right){
                total=Math.max(total,left*2);
            }else if(right>left){
                left=right=0;
            }
        }
        left=right=0;
        for(int i=s.length()-1;i>=0;i--){
            if(s.charAt(i)=='('){
                left++;
            }else if(s.charAt(i)==')'){
                right++;
            }
            if(left==right){
                total=Math.max(total,right*2);
            }else if(right<left){
                left=right=0;
            }
        }
        return total;
    }
}
