package com.shizijie.dev.helper.web.leetcode;

import com.sun.org.apache.bcel.internal.generic.POP;

/**
 * @author shizijie
 * @version 2020-03-31 下午10:17
 *
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。

示例 1:

输入: 123
输出: 321

示例 2:

输入: -123
输出: -321

示例 3:

输入: 120
输出: 21

 *
 */
public class L7 {

    public int reverse(int x) {
        int rev=0;
        while (x!=0){
            int pop=x%10;
            x/=10;
            if(rev>Integer.MAX_VALUE/10||(rev==Integer.MAX_VALUE/10&&pop>7)){
                return 0;
            }
            if(rev<Integer.MIN_VALUE/10||(rev==Integer.MIN_VALUE/10&&pop<-8)){
                return 0;
            }
            rev=rev*10+pop;
        }
        return rev;
    }

}
