package com.shizijie.dev.helper.web.leetcode.test2;

import com.alibaba.fastjson.JSON;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L024 {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }


    public ListNode swapPairs(ListNode head) {
        if(head==null||head.next==null){
            return head;
        }
        ListNode tmp=head.next;
        head.next=swapPairs(tmp.next);
        tmp.next=head;
        return tmp;
    }

    public static void main(String[] args) {
        L024 l024=new L024();
        ListNode thrid=new ListNode(2);
        ListNode second= new ListNode(1,thrid);
        ListNode first= new ListNode(0,second);
        ListNode res=l024.swapPairs(first);
        System.out.println(JSON.toJSONString(res));
    }
}
