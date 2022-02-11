package com.shizijie.dev.helper.web.leetcode.test4;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:53
 */
public class L064 {
    public int minPathSum(int[][] grid) {
        if(grid==null||grid.length==0||grid[0].length==0){
            return 0;
        }
        int rows=grid.length;
        int cols=grid[0].length;
        int[][] arr=new int[rows][cols];
        arr[0][0]=grid[0][0];
        for(int i=1;i<rows;i++){
            arr[i][0]=grid[i][0]+arr[i-1][0];
        }
        for(int i=1;i<cols;i++){
            arr[0][i]=grid[0][i]+arr[0][i-1];
        }
        for(int i=1;i<rows;i++){
            for(int j=1;j<cols;j++){
                int num=grid[i][j];
                arr[i][j]=Math.min(arr[i-1][j],arr[i][j-1])+num;
            }
        }
        return arr[rows-1][cols-1];
    }
}
