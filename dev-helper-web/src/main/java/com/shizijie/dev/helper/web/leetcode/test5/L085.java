package com.shizijie.dev.helper.web.leetcode.test5;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:55
 */
public class L085 {
    public boolean []isVisited; // 判断是否被访问过
    public int count = 0; // 计数
    public int findCircleNum(int[][] isConnected) {
        isVisited = new boolean[isConnected.length];
        // 遍历每个城市
        for (int i = 0; i < isConnected.length; i++) {
            if (!isVisited[i]) {
                dfs(i, isConnected);
                count++;
            }
        }
        return count;
    }

    private void dfs(int i,int[][] isConnected) {
        isVisited[i] = true; // 该城市已被访问
        for (int k = 0; k < isConnected.length; k++) {
            if (i == k) continue; // 如果是自身就跳过
            if (isConnected[i][k] == 1 && !isVisited[k]) {
                dfs(k, isConnected); // 深搜
            }
        }

    }
}
