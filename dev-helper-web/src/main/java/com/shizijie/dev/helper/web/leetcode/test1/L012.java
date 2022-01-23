package com.shizijie.dev.helper.web.leetcode.test1;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L012 {


    public String intToRoman(int num) {
        Map<Integer,String> map=new LinkedHashMap<>();
        map.put(1000,"M");
        map.put(900,"CM");
        map.put(500,"D");
        map.put(400,"CD");
        map.put(100,"C");
        map.put(90,"XC");
        map.put(50,"L");
        map.put(40,"XL");
        map.put(10,"X");
        map.put(9,"IX");
        map.put(5,"V");
        map.put(4,"IV");
        map.put(1,"I");
        StringBuilder s=new StringBuilder();
        for(Map.Entry<Integer,String> entry:map.entrySet()){
            while (num>=entry.getKey()){
                num-=entry.getKey();
                s.append(entry.getValue());
            }
            if(num==0){
                break;
            }
        }
        return s.toString();
    }
}
