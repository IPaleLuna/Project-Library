package org.example.sorting;

import java.util.Comparator;
import java.util.List;

public class Sorter<T extends Comparable<? super T>> implements Sort<T> {
    private Sort sort;

    public Sorter() {
        this(new ParallelQuickSort());
    }

    public Sorter(Sort sort) {
        if (sort == null) {
            throw new NullPointerException();
        }

        this.sort = sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public void sort(List<T> list) {
        sort.sort(list);
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        sort.sort(list, comparator);
    }
}
