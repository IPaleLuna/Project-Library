package org.example.sorting;

import java.util.*;
import java.util.concurrent.*;

public class ParallelTimSort<T extends Comparable<? super T>> implements Sort<T> {
    private final ForkJoinPool pool;
    private static final int MIN_RUN = 32;

    public ParallelTimSort() {
        this(2);
    }

    public ParallelTimSort(int threadCount) {
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
            List<T> sorted = pool.invoke(new TimSortTask<>(list, comparator));
            for (int i = 0; i < list.size(); i++) {
                list.set(i, sorted.get(i));
            }
        } finally {
            pool.shutdown();
        }
    }

    private static class TimSortTask<T> extends RecursiveTask<List<T>> {
        private final List<T> list;
        private final Comparator<T> comparator;

        TimSortTask(List<T> list, Comparator<T> comparator) {
            this.list = list;
            this.comparator = comparator;
        }

        @Override
        protected List<T> compute() {
            int n = list.size();
            if (n <= 1) return new ArrayList<>(list);

            List<List<T>> runs = new ArrayList<>();
            for (int i = 0; i < n; i += MIN_RUN) {
                int end = Math.min(i + MIN_RUN, n);
                List<T> subList = new ArrayList<>(list.subList(i, end));
                subList.sort(comparator);
                runs.add(subList);
            }

            while (runs.size() > 1) {
                List<ForkJoinTask<List<T>>> mergeTasks = new ArrayList<>();

                for (int i = 0; i < runs.size(); i += 2) {
                    if (i + 1 < runs.size()) {
                        List<T> left = runs.get(i);
                        List<T> right = runs.get(i + 1);
                        mergeTasks.add(new MergeTask<>(left, right, comparator).fork());
                    } else {
                        List<T> last = runs.get(i);
                        mergeTasks.add(ForkJoinTask.adapt(() -> last));
                    }
                }

                List<List<T>> mergedRuns = new ArrayList<>();
                for (ForkJoinTask<List<T>> task : mergeTasks) {
                    mergedRuns.add(task.join());
                }

                runs = mergedRuns;
            }

            return runs.get(0);
        }
    }

    private static class MergeTask<T> extends RecursiveTask<List<T>> {
        private final List<T> left;
        private final List<T> right;
        private final Comparator<T> comparator;

        MergeTask(List<T> left, List<T> right, Comparator<T> comparator) {
            this.left = left;
            this.right = right;
            this.comparator = comparator;
        }

        @Override
        protected List<T> compute() {
            return merge(left, right, comparator);
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
