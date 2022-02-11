package com.shizijie.dev.helper.web.leetcode.test4;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:53
 */
public class L065 {
    public int[] plusOne(int[] digits) {
        for(int i=digits.length-1;i>=0;i--){
            digits[i]=(digits[i]+1)%10;
            if(digits[i]!=0){
               return digits;
            }
        }
        int[] newArr=new int[digits.length+1];
        newArr[0]=1;
        return newArr;
    }
}
