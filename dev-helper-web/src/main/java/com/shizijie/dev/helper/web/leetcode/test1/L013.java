package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 转数字
 */
public class L013 {
    public int romanToInt(String s) {
        String access = s;
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
        int total = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            while (s.startsWith(entry.getValue())) {
                s = s.substring(entry.getValue().length());
                total += entry.getKey();
            }
            if (s.equals("")) {
                break;
            }
        }
        System.out.println(access);
        return total;
    }
}
