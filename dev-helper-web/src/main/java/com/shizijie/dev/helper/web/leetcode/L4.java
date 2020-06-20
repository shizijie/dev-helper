package com.shizijie.dev.helper.web.leetcode;

/**
 * @author shizijie
 * @version 2020-03-30 下午10:42
 *
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。

请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。

你可以假设 nums1 和 nums2 不会同时为空。

示例 1:

nums1 = [1, 3]
nums2 = [2]

则中位数是 2.0

示例 2:

nums1 = [1, 2]
nums2 = [3, 4]

则中位数是 (2 + 3)/2 = 2.5

 *
 */
public class L4 {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m=nums1.length,n=nums2.length;
        if(m>n){
            int[] tmpArr=nums1;
            nums1=nums2;
            nums2=tmpArr;
            int tmp=m;
            m=n;
            n=tmp;
        }
        int iMin=0,iMax=m,len=(m+n+1)/2;
        while (iMin<=iMax){
            int i=(iMin+iMax)/2;
            int j=len-i;
            if(i<iMax&&nums2[j-1]>nums1[i]){
                iMin=i+1;
            }else if(i>iMin&&nums1[i-1]>nums2[j]){
                iMax=i-1;
            }else{
                int maxLeft=0;
                if(i==0){
                    maxLeft=nums2[j-1];
                }else if(j==0){
                    maxLeft=nums1[i-1];
                }else{
                    maxLeft=Math.max(nums1[i-1],nums2[j-1]);
                }
                if((m+n)%2==1){
                    return maxLeft;
                }
                int minRight=0;
                if(i==m){
                    minRight=nums2[j];
                }else if(j==n){
                    minRight=nums1[i];
                }else{
                    minRight=Math.min(nums1[i],nums2[j]);
                }
                return (maxLeft+minRight)/2.0;
            }
        }
        return 0.0;
    }

}
