package com.shizijie.dev.helper.web.leetcode.test3;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L050 {
    public double myPow(double x, int n) {
        // 迭代算法，利用二进制位
        if(x == 0){ // 0 的任何次方都等于 0,1 的任何次方都等于 1
            return x;
        }

        long power = n;    // 为了保证-n不溢出，先转换成long类型
        if(n < 0){         // 如果n小于0， 求1/x的-n次方
            power *= -1;
            x = 1 / x;
        }
        double weight = x;  // 权值初值为x, 即二进制位第1位的权值为x^1
        double res = 1;
        while(power != 0){
            // 如果当前二进制位为1， 让结果乘上这个二进制位上的权值,
            // 该位权值在上一轮迭代中已经计算出来了
            if((power & 1) == 1){
                res *= weight;
            }
            weight *= weight;   // 计算下一个二进制位的权值
            power /= 2;
        }
        return res;
    }
}
