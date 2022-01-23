package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L003 {
    public int lengthOfLongestSubstring(String s) {
        if(s==null){
            return 0;
        }
        if(s.length()<=1){
            return s.length();
        }
        String res=s.substring(0,1);
        for(int start=0,end=1;end<s.length();end++){
            char var=s.charAt(end);
            int newStart=s.indexOf(var,start);
            if(newStart<end){
                start=newStart+1;
            }
            if(end-start+1>res.length()){
                res=s.substring(start,end+1);
            }
        }
        return res.length();
    }
}
