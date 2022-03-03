package com.shizijie.dev.helper.web.demo.lambda;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shizijie
 * @version 2022-03-03 11:55 PM
 */
public class ListToMap {
    @Data
    public static class User{
        private long id;
        private String name;
    }

    public static void main(String[] args) {
        List<User> list=new ArrayList<>();
        User user=new User();
        user.setId(1);
        user.setName("xl");
        list.add(user);
        Map<Long,List<String>> map=list.stream().collect(Collectors.groupingBy(User::getId,Collectors.mapping(User::getName,Collectors.toList())));
        System.out.println(map);
    }
}
