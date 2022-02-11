package com.shizijie.dev.helper.web.leetcode.test3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:52
 */
public class L057 {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        if(newInterval.length == 0) return intervals;
        if(intervals.length == 0) return new int[][]{newInterval};
        List<int[]> res = new ArrayList<>();
        int L = newInterval[0], R = newInterval[1], len = intervals.length, j = 0;
        while(j < len && intervals[j][1] < L) res.add(intervals[j++]);      //重叠前直接加入List
        while(j < len && intervals[j][0] <= R){     //处理重叠区域
            L = Math.min(L, intervals[j][0]);
            R = Math.max(R, intervals[j++][1]);
        }
        res.add(new int[]{L,R});
        while(j < len) res.add(intervals[j++]);     //不会再出现重叠区域，直接加入List
        return res.toArray(new int[res.size()][]);
    }
}
