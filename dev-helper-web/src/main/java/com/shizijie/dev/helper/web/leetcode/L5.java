package com.shizijie.dev.helper.web.leetcode;

/**
 * @author shizijie
 * @version 2020-03-31 下午9:43
 *
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。

示例 1：

输入: "babad"
输出: "bab"
注意: "aba" 也是一个有效答案。

示例 2：

输入: "cbbd"
输出: "bb"


 *
 */
public class L5 {
    public String longestPalindrome(String s) {
        if(s==null|s.length()<=1){
            return "";
        }
        int start=0,end=0;
        for(int i=0;i<s.length();i++){
            int len1=getCenter(s,i,i);
            int len2=getCenter(s,i,i+1);
            int len=Math.max(len1,len2);
            if(len>end-start){
                start=i-(len-1)/2;
                end=i+len/2;
            }
        }
        return s.substring(start,end+1);
    }

    public int getCenter(String s,int left,int right){
        int L=left;
        int R=right;
        while (L>=0&&R<s.length()&&s.charAt(L)==s.charAt(R)){
            L--;
            R++;
        }
        return R-L-1;
    }
}
