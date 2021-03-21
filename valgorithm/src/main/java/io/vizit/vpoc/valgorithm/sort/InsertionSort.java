package io.vizit.vpoc.valgorithm.sort;

import java.util.Arrays;

public class InsertionSort {
    public static void main(String[] args) {
//        int[] A = new int[]{5, 2, 4, 6, 1, 3};
//        int[] A = new int[]{1, 2, 3, 4, 5, 6};
        int[] A = new int[]{6, 5, 4, 3, 2, 1};
        System.out.println("======== before sorted: " + Arrays.toString(A));
        insertionSort(A);
        System.out.println("======== after sorted: " + Arrays.toString(A));
    }

    private static void insertionSort(int[] A) {
        for (int j = 1; j < A.length; j++) { // n
            System.out.println("j =================== " + j);
            int key = A[j]; // n-1
            int i = j - 1;  // n-1
            while (i >= 0 && A[i] > key) {
                System.out.print("-> ");
                A[i + 1] = A[i]; // n * n
                i--;
            }
            System.out.println("key is moved to " + (i + 1));
            A[i + 1] = key; // n-1
        }
    }
}
