package org.example.app;

import org.example.sorting.ParallelMergeSort;
import org.example.sorting.ParallelQuickSort;

import org.example.sorting.ParallelTimSort;
import org.example.sorting.Sort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelQuickSort quickSort = new ParallelQuickSort();
        quickSort.sort(list1);

        for (int i = 0; i < list1.size(); i++) {
            System.out.printf(list1.get(i) + " ");
        }

        System.out.println();

        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelMergeSort mergeSort = new ParallelMergeSort();
        mergeSort.sort(list2);

        for (int i = 0; i < list2.size(); i++) {
            System.out.printf(list2.get(i) + " ");
        }

        System.out.println();

        List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelTimSort timSort = new ParallelTimSort();
        timSort.sort(list3);

        for (int i = 0; i < list3.size(); i++) {
            System.out.printf(list3.get(i) + " ");
        }

        System.out.println();
    }
}
