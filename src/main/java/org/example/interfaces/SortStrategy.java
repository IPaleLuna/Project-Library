package org.example.interfaces;

import java.util.List;

public interface SortStrategy<T> {
    void sort(List<T> list);
}