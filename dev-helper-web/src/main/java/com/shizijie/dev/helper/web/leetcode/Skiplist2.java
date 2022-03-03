package com.shizijie.dev.helper.web.leetcode;

import java.util.PriorityQueue;

/**
 * @author shizijie
 * @version 2022-02-12 9:28 PM
 */
public class Skiplist2 {
    class Node {
        Integer value;
        Node[] next;

        public Node(Integer value, Integer size) {
            this.value = value;
            this.next = new Node[size];
        }
    }

    private static final Integer MAX_LEVEL = 32;
    private static final double factor = 0.25;

    private Node head;
    private Integer curLevel;


    public Skiplist2() {
        head = new Node(null, MAX_LEVEL);
        curLevel = 1;
    }

    private Integer randomLevel() {
        int level = 1;
        while (Math.random() < factor && level < MAX_LEVEL) {
            ++level;
        }
        return level;
    }

    private Node findNode(Node cur, int level, int value) {
        while (cur.next[level] != null && cur.next[level].value < value) {
            cur = cur.next[level];
        }
        return cur;
    }

    public boolean search(int target) {
        Node start = head;
        for (int i = curLevel - 1; i >= 0; --i) {
            start = findNode(start, i, target);
            if (start.next[i] != null && start.next[i].value == target) {
                return true;
            }
        }
        return false;
    }

    public void add(int num) {
        int level = randomLevel();
        Node start = head;
        Node newNode = new Node(num, level);
        for (int i = curLevel - 1; i >= 0; --i) {
            start = findNode(start, i, num);
            if (i < level) {
                if (start.next[i] == null) {
                    start.next[i] = newNode;
                } else {
                    Node tmp = start.next[i];
                    start.next[i] = newNode;
                    newNode.next[i] = tmp;
                }
            }
        }
        if (level > curLevel) {
            for (int i = curLevel; i < level; ++i) {
                head.next[i] = newNode;
            }
            curLevel = level;
        }
    }

    public boolean erase(int num) {
        boolean flag = false;
        Node start = head;
        for (int i = curLevel - 1; i >= 0; --i) {
            start = findNode(start, i, num);
            if (start.next[i] != null && start.next[i].value == num) {
                start.next[i] = start.next[i].next[i];
                flag = true;
            }
        }
        return flag;
    }
}
