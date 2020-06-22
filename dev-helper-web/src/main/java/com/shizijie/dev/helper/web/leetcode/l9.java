package com.shizijie.dev.helper.web.leetcode;

/**
 * @author shizijie
 * @version 2020-04-01 下午2:00
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。

示例 1:

输入: 121
输出: true

示例 2:

输入: -121
输出: false
解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。

示例 3:

输入: 10
输出: false
解释: 从右向左读, 为 01 。因此它不是一个回文数。


 *
 */
public class l9 {
    public static void main(String[] args) {
        System.out.println(isPalindrome(121));
    }
    public static boolean isPalindrome(int x) {
        if(x<0||(x % 10 == 0 && x != 0)){
            return false;
        }
        int res=0;
        int tmp=x;
        while (tmp!=0){
            int pop=tmp%10;
            tmp/=10;
            res=res*10+pop;
        }
        return res==x;
    }
}