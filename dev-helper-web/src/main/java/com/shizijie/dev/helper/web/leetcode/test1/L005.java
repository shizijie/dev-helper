package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L005 {
    public static void main(String[] args) {
        L005 l = new L005();
        System.out.println(l.longestPalindrome("ccc"));
    }


    public String longestPalindrome(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() <= 1) {
            return s;
        }
        String res = s.substring(0, 1);
        for (int i = 0; i < s.length() - 1; i++) {
            Integer s1=isHuiWen(i,i,s);
            Integer s2=isHuiWen(i,i+1,s);
            Integer tmp=s1;
            if(s2>s1){
                tmp=s2;
            }
            if(tmp>res.length()){
                res=s.substring(i-(tmp-1)/2,i+tmp/2+1);
            }
        }
        return res;
    }

    public int isHuiWen(int start, int end, String s) {
        while (start>=0 && start <= end && end < s.length() && s.charAt(start) == s.charAt(end)) {
            start--;
            end++;
        }
        return end-start-1;
    }
}
