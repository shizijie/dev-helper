package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 最长前缀
 */
public class L014 {

    public String longestCommonPrefix(String[] strs) {
        int access = strs.length;
        int total = strs[0].length();
        for (String s : strs) {
            total = Math.min(s.length(), total);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < total; i++) {
            char charAt = strs[0].charAt(i);
            for (String s : strs) {
                if (s.charAt(i) != charAt) {
                    return sb.toString();
                }
            }
            sb.append(charAt);
        }
        System.out.println(access);
        return sb.toString();
    }
}
