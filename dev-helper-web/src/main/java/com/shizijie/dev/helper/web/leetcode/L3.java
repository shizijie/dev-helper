package com.shizijie.dev.helper.web.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2020-03-30 下午10:00
 *
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。

示例 1:

输入: "abcabcbb"
输出: 3
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

示例 2:

输入: "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。

示例 3:

输入: "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

 *
 */
public class L3 {
    public int lengthOfLongestSubstring(String s) {
        if(s.length()<1){
            return 0;
        }
        List<Character> list=new ArrayList<>();
        int max=0;
        int head=0,tail=0;
        for(int i=0;i<s.length();i++){
            if(list.contains(s.charAt(i))){
                max=Math.max(max,list.size());
                for(int r=0;r<=list.indexOf(s.charAt(i));r++){
                    list.remove(r--);
                }
            }
            list.add(s.charAt(i));
        }
        max=Math.max(max,list.size());
        return max;
    }

    public int lengthOfLongestSubstring2(String s) {
        if(s.length()<=1){
            return s.length();
        }
        int max=0;
        for(int head=0,tail=1;tail<s.length();tail++){
            char a=s.charAt(tail);
            int index=s.indexOf(a,head);
            if(index<tail){
                head=index+1;
            }
            int length=tail-head+1;
            max=Math.max(length,max);
        }
        return max;
    }
}
