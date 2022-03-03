package com.shizijie.dev.helper.web.demo.format;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author shizijie
 * @version 2022-02-19 12:51 AM
 */
public class LogFormat {
    public static void main(String[] args) {
        Message message=new ParameterizedMessageFactory().newMessage("lll{}",123);
        System.out.println(message.getFormattedMessage());
        Queue<Integer> queue=new LinkedList<>();
        Arrays.sort("".toCharArray());
        PriorityQueue<Character> queu2=new PriorityQueue<>();
    }
}
