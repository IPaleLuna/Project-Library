package org.example.sorting;

import java.util.*;
import java.util.concurrent.*;

public class ParallelMergeSort<T extends Comparable<? super T>> implements Sort<T> {
    private final ForkJoinPool pool;

    public ParallelMergeSort() {
        this(2);
    }

    public ParallelMergeSort(int threadCount) {
        this.pool = new ForkJoinPool(Math.max(threadCount, 2));
    }

    @Override
    public void sort(List<T> list) {
        sort(list, Comparator.naturalOrder());
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        try {
            List<T> sorted = pool.invoke(new MergeSortTask<>(list, comparator));
            for (int i = 0; i < list.size(); i++) {
                list.set(i, sorted.get(i));
            }
        } finally {
            pool.shutdown();
        }
    }

    private static class MergeSortTask<T> extends RecursiveTask<List<T>> {
        private static final int SEQUENTIAL_THRESHOLD = 50; // Test value
        private final List<T> list;
        private final Comparator<T> comparator;

        MergeSortTask(List<T> list, Comparator<T> comparator) {
            this.list = list;
            this.comparator = comparator;
        }

        @Override
        protected List<T> compute() {
            int size = list.size();
            if (size <= 1) {
                return new ArrayList<>(list);
            }

            if (size < SEQUENTIAL_THRESHOLD) {
                List<T> copy = new ArrayList<>(list);
                copy.sort(comparator);
                return copy;
            }

            int mid = size / 2;
            List<T> left = list.subList(0, mid);
            List<T> right = list.subList(mid, size);

            MergeSortTask<T> leftTask = new MergeSortTask<>(left, comparator);
            MergeSortTask<T> rightTask = new MergeSortTask<>(right, comparator);

            leftTask.fork();

            List<T> rightResult = rightTask.compute();
            List<T> leftResult = leftTask.join();

            return merge(leftResult, rightResult, comparator);
        }

        private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
            List<T> merged = new ArrayList<>(left.size() + right.size());
            int i = 0, j = 0;

            while (i < left.size() && j < right.size()) {
                if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                    merged.add(left.get(i++));
                } else {
                    merged.add(right.get(j++));
                }
            }

            while (i < left.size()) merged.add(left.get(i++));
            while (j < right.size()) merged.add(right.get(j++));
            return merged;
        }
    }
}
