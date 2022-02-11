package com.shizijie.dev.helper.web.leetcode.test5;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:55
 */
public class L087 {
    public static void main(String[] args) {
        StringBuffer sb=new StringBuffer();
        sb.deleteCharAt(sb.length()-1);
    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }


    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode res=new ListNode(0);
        ListNode tmp=res;
        dfs(head,0,k,res,new ListNode());
        return tmp.next;
    }
    public void dfs(ListNode head,int start,int k,ListNode res,ListNode tmp){
        if(head==null){
            return;
        }
        if(start%k==0){
            res.next=tmp.next;
            res=res.next;
            tmp=new ListNode(0);
        }
        ListNode node=head.next;
        head.next=tmp;
        tmp=head;
        head=node;
        dfs(head,start+1,k,res,tmp);


    }
}
