package com.shizijie.dev.helper.web.leetcode;

import lombok.var;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author shizijie
 * @version 2020-03-30 下午10:00
 *
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。

示例 1:

输入: "abcabcbb"
输出: 3
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

示例 2:

输入: "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。

示例 3:

输入: "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

 *
 */
public class L3 {
    public int lengthOfLongestSubstring(String s) {
        if(s.length()<1){
            return 0;
        }
        List<Character> list=new ArrayList<>();
        int max=0;
        int head=0,tail=0;
        for(int i=0;i<s.length();i++){
            if(list.contains(s.charAt(i))){
                max=Math.max(max,list.size());
                for(int r=0;r<=list.indexOf(s.charAt(i));r++){
                    list.remove(r--);
                }
            }
            list.add(s.charAt(i));
        }
        max=Math.max(max,list.size());
        return max;
    }

    public int lengthOfLongestSubstring2(String s) {
        if(s.length()<=1){
            return s.length();
        }
        int max=0;
        for(int head=0,tail=1;tail<s.length();tail++){
            char a=s.charAt(tail);
            int index=s.indexOf(a,head);
            if(index<tail){
                head=index+1;
            }
            int length=tail-head+1;
            max=Math.max(length,max);
        }
        return max;
    }

    /**
     *   将 \textit{nums}nums 拆分成两部分，左半部分的最小和（前缀最小和）减右半部分的最大和（后缀最大和）即为两部分和的最小差值，枚举拆分位置（保证左右两部分至少有 nn 个元素），所有差值的最小值就是答案。

         因此我们需要计算出 \textit{nums}nums 的前缀最小和，即前 ii 个元素中的最小的 nn 个元素之和，以及后缀最大和，即后 ii 个元素中的最大的 nn 个元素之和。

         计算前缀最小和时，可以维护一个包含 nn 个元素的最大堆，我们不断向右遍历 \textit{nums}nums 中的元素 vv，计算前缀最小和，若 vv 比堆顶元素小，则弹出堆顶元素，并将 vv 入堆。

         计算后缀最大和，则需要维护一个包含 nn 个元素的最小堆，我们不断向左遍历 \textit{nums}nums 中的元素 vv，计算后缀最大和，若 vv 比堆顶元素大，则弹出堆顶元素，并将 vv 入堆。
     * @param nums
     * @return
     */
    public long minimumDifference(int[] nums) {
        var m = nums.length;
        var n = m / 3;
        var minPQ = new PriorityQueue<Integer>();
        var sum = 0L;
        // 循环2n个 并相加
        for (var i = m - n; i < m; i++) {
            minPQ.add(nums[i]);
            sum += nums[i];
        }
        var sufMax = new long[m - n + 1]; // 后缀最大和
        //第m-n之和
        sufMax[m - n] = sum;
        // n - m-n-1
        for (var i = m - n - 1; i >= n; --i) {
            minPQ.add(nums[i]);
            sum += nums[i] - minPQ.poll();
            sufMax[i] = sum;
        }

        var maxPQ = new PriorityQueue<Integer>(Collections.reverseOrder());
        var preMin = 0L; // 前缀最小和
        for (var i = 0; i < n; ++i) {
            maxPQ.add(nums[i]);
            preMin += nums[i];
        }
        var ans = preMin - sufMax[n];
        for (var i = n; i < m - n; ++i) {
            maxPQ.add(nums[i]);
            preMin += nums[i] - maxPQ.poll();
            ans = Math.min(ans, preMin - sufMax[i + 1]);
        }
        return ans;
    }

    public static void main(String[] args) {
        var minPQ = new PriorityQueue<Integer>();
        minPQ.add(1);
        minPQ.add(4);
        minPQ.add(3);
        minPQ.add(0);
        System.out.println(minPQ.poll());
        List<Integer> list=new ArrayList<>(minPQ);
        System.out.println(list.get(2));
        System.out.println(minPQ.size());
    }
}
