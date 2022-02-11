package com.shizijie.dev.helper.web.leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * @author shizijie
 * @version 2022-02-07 8:33 PM
 */
public class Trie {

    private Trie[] children;

    private boolean isEnd;


    public Trie() {
        children=new Trie[26];
        isEnd=false;

    }

    public void insert(String word) {
        Trie node=this;
        for(int i=0;i<word.length();++i){
            char c=word.charAt(i);
            int index=c-'a';
            if(node.children[index]==null){
                node.children[index]=new Trie();
            }
            node=node.children[index];
        }
        node.isEnd=true;
    }

    public boolean search(String word) {
        Trie node=startsPrefix(word);
        return node!=null&&node.isEnd;
    }



    public boolean startsWith(String prefix) {
        return startsPrefix(prefix)!=null;
    }

    private Trie startsPrefix(String prefix) {
        Trie node=this;
        for(int i=0;i<prefix.length();++i){
            int index=prefix.charAt(i)-'a';
            if(node.children[index]==null){
                return null;
            }
            node=node.children[index];
        }
        return node;
    }


    public boolean containsDuplicate(int[] nums) {
        return (new HashSet(Arrays.asList(nums))).size()!=nums.length;
    }

}
