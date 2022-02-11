package com.shizijie.dev.helper.web.leetcode.test4;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:53
 */
public class L067 {
    public String addBinary(String a, String b) {
        if(a.length()<b.length()){
            String tmp=a;
            a=b;
            b=tmp;
        }
        StringBuilder sb=new StringBuilder();
        char pre='0';
        for(int i=0;i<a.length();i++){
            char btmp;
            if(i>=b.length()){
                btmp='0';
            }else{
                btmp=b.charAt(i);
            }
            sb.append((a.charAt(i)^(btmp|pre)));
            if(a.charAt(i)+btmp+pre>'1'+'0'+'0'){
                pre='1';
            }
        }
        if(pre!='0'){
            sb.append(pre);
        }
        return sb.reverse().toString();
    }

}
