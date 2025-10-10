package org.example.app;

import org.example.sorting.ParallelQuickSort;

import org.example.sorting.Sort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelQuickSort sort = new ParallelQuickSort();
        sort.sort(list);

        for (int i = 0; i < list.size(); i++) {
            System.out.printf(list.get(i) + " ");
        }

        System.out.println();
    }
}
