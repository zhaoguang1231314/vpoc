package io.vizit.vpoc.valgorithm.sort;

import java.util.Arrays;

public class MergeSort {

    public static void main(String[] args) {
        int[] array = new int[]{5, 2, 4, 7, 1, 3, 2, 6};
        System.out.println("======== before sorted: " + Arrays.toString(array));
        mergeSort(array, 0, array.length - 1);
        System.out.println("======== after sorted: " + Arrays.toString(array));
    }

    public static void mergeSort(int[] array, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(array, low, mid); //递归地对左边进行排序
            mergeSort(array, mid + 1, high); // //递归地对右边进行排序

            merge(array, low, mid, high);//合并
        }
    }

    private static void merge(int[] array, int low, int mid, int high) {
        int leftLength = mid - low + 1;
        int rightLength = high - mid;
        int[] leftArray = new int[leftLength];// 左边数组
        int[] rightArray = new int[rightLength];// 右边数组

        for (int i = 0; i < leftLength; i++) {
            leftArray[i] = array[low + i];
        }
        for (int i = 0; i < rightLength; i++) {
            rightArray[i] = array[mid + 1 + i];
        }
        for (int left = 0, right = 0, i = low; i <= high; i++) {
            if (left == leftLength) {
                array[i] = rightArray[right];
                right++;
                continue;
            }
            if (right == rightLength) {
                array[i] = leftArray[left];
                left++;
                continue;
            }
            if (leftArray[left] < rightArray[right]) {
                array[i] = leftArray[left];
                left++;
            } else {
                array[i] = rightArray[right];
                right++;
            }
        }
    }
}
