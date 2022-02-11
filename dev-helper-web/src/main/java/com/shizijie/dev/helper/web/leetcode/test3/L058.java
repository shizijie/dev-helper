package com.shizijie.dev.helper.web.leetcode.test3;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L058 {
    public int lengthOfLastWord(String s) {
        int index = s.length() - 1;
        while (s.charAt(index) == ' ') {
            index--;
        }
        int wordLength = 0;
        while (index >= 0 && s.charAt(index) != ' ') {
            wordLength++;
            index--;
        }
        return wordLength;
    }
}
