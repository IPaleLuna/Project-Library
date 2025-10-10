package org.example.sorting;

import java.util.Comparator;
import java.util.List;

public interface Sort<T extends Comparable<? super T>> {
    void sort(List<T> list);
    void sort(List<T> list, Comparator<T> comparator);
}
