package org.example.sorting;

import java.util.*;
import java.util.concurrent.*;

public class ParallelQuickSort<T extends Comparable<? super T>> implements Sort<T> {
    private final ForkJoinPool pool;

    public ParallelQuickSort() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ParallelQuickSort(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be greater than zero.");
        }
        this.pool = new ForkJoinPool(threadCount);
    }

    @Override
    public void sort(List<T> list) {
        sort(list, Comparator.naturalOrder());
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() < 2) {
            return;
        }

        pool.invoke(new QuickSortTask<>(list, 0, list.size() - 1, comparator));
        pool.shutdown();
    }

    private static class QuickSortTask<T> extends RecursiveAction {
        private static final int SEQUENTIAL_THRESHOLD = 500;
        private final List<T> list;
        private final int low;
        private final int high;
        private final Comparator<T> comparator;

        QuickSortTask(List<T> list, int low, int high, Comparator<T> comparator) {
            this.list = list;
            this.low = low;
            this.high = high;
            this.comparator = comparator;
        }

        @Override
        protected void compute() {
            if (low < high) {
                if (high - low < SEQUENTIAL_THRESHOLD) {

                    quickSortSequential(list, low, high, comparator);
                } else {
                    int pivotIndex = partition(list, low, high, comparator);
                    QuickSortTask<T> leftTask = new QuickSortTask<>(list, low, pivotIndex - 1, comparator);
                    QuickSortTask<T> rightTask = new QuickSortTask<>(list, pivotIndex + 1, high, comparator);
                    invokeAll(leftTask, rightTask);
                }
            }
        }

        private static <T> void quickSortSequential(List<T> list, int low, int high, Comparator<T> comparator) {
            if (low < high) {
                int pivotIndex = partition(list, low, high, comparator);
                quickSortSequential(list, low, pivotIndex - 1, comparator);
                quickSortSequential(list, pivotIndex + 1, high, comparator);
            }
        }

        private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
            T pivot = list.get(high);
            int i = low - 1;

            for (int j = low; j < high; j++) {
                if (comparator.compare(list.get(j), pivot) <= 0) {
                    i++;
                    Collections.swap(list, i, j);
                }
            }

            Collections.swap(list, i + 1, high);
            return i + 1;
        }
    }
}
