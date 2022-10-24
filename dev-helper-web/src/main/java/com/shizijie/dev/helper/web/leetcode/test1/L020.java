package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 是否有效
 */
public class L020 {
    public boolean isValid(String s) {
        String pet = s;
        Map<Character, Character> map = new HashMap<>();
        map.put('}', '{');
        map.put(')', '(');
        map.put(']', '[');
        Deque<Character> stock = new LinkedList();
        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                if (stock.isEmpty() || stock.peek() != map.get(c)) {
                    return false;
                }
                stock.pop();
            } else {
                stock.push(c);
            }
        }
        System.out.println(pet);
        return stock.isEmpty();
    }
}
