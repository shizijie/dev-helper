package com.shizijie.dev.helper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shizijie
 * @version 2019-11-12 下午6:58
 */
public class NameUtils {
    private final static Pattern linePattern = Pattern.compile("_(\\w)");
    /** 下划线转驼峰（首字母大写） */
    public static String lineToHump(String str) {
        str = str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 驼峰转下划线 */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }
}
