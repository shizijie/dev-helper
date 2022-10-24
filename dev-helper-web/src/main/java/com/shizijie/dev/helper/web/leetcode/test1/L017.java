package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 转换4
 */
public class L017 {
    public List<String> letterCombinations(String digits) {
        String model = digits;
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        list.add("pqrs");
        list.add("tuv");
        list.add("wxyz");
        List<String> res = new ArrayList<>();
        if (digits == null || digits.length() < 1) {
            return res;
        }
        StringBuilder sb = new StringBuilder();

        setValue(list, digits, 0, res, sb);
        System.out.println(model);
        return res;
    }

    public void setValue(List<String> tmp, String digits, int index, List<String> res, StringBuilder sb) {
        if (index == digits.length()) {
            res.add(sb.toString());
            return;
        } else {
            int num = digits.charAt(index) - '0';
            String str = tmp.get(num);
            char[] arr = str.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                setValue(tmp, digits, index + 1, res, sb);
                sb.deleteCharAt(index);
            }
        }
    }
}
