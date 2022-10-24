package com.shizijie.dev.helper.web.leetcode.test1;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 * 链转换
 */
public class L019 {
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

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode guide = new ListNode(0, head);
        ListNode res = guide;
        int length = 0;
        while (head != null) {
            length++;
            head = head.next;
        }
        ListNode tmp = res;
        for (int i = 0; i < length - n; i++) {
            tmp = tmp.next;
        }
        tmp.next = tmp.next.next;
        System.out.println(guide);
        return res.next;
    }
}
