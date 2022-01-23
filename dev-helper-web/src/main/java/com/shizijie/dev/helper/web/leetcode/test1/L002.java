package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L002 {
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }
        ListNode(int val) {
            this.val = val;
        }
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res=new ListNode(0);
        ListNode tmp=res;
        int top=0;
        while (l1!=null || l2!=null || top>0){
            int val1=l1==null?0:l1.val;
            int val2=l2==null?0:l2.val;
            int val=(top+val1+val2)%10;
            top=(top+val1+val2)/10;
            ListNode node=new ListNode(val);
            tmp.next=node;
            tmp=tmp.next;
            if(l1!=null){
                l1=l1.next;
            }
            if(l2!=null){
                l2=l2.next;
            }
        }
        return res.next;
    }
}
