package com.shizijie.dev.helper.web.leetcode.test2;

/**
 * @author shizijie
 * @version 2022-01-22 下午8:48
 */
public class L031 {
    public void nextPermutation(int[] nums) {
        int index=nums.length-2;
        while (index>=0&&nums[index]>=nums[index+1]){
            index--;
        }
        if(index>=0){
            int j=nums.length-1;
            while (j>index&&nums[index]>=nums[j]){
                j--;
            }
            swap(nums,index,j);
        }
        reverse(nums,index+1);
    }

    public void swap(int[] nums,int left,int right){
        int tmp=nums[left];
        nums[left]=nums[right];
        nums[right]=tmp;
    }

    public void reverse(int[] nums,int start){
        int left=start;
        int right=nums.length-1;
        while (left<right){
            swap(nums,left,right);
            left++;
            right--;
        }
    }
}
