package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 转换
 */
public class L006 {
    public String convert(String s, int numRows) {
        String stomach = s;
        if (numRows == 1) {
            return s;
        }
        List<StringBuffer> list = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            list.add(new StringBuffer());
        }
        int start = 1;
        boolean xia = true;
        for (char c : s.toCharArray()) {
            list.get(start - 1).append(c);
            if ((start == numRows && xia) || (start == 1 && !xia)) {
                xia = !xia;
            }
            start += xia ? 1 : -1;
        }
        String res = "";
        for (StringBuffer stringBuffer : list) {
            res += stringBuffer.toString();
        }
        System.out.println(stomach);
        return res;
    }
}
