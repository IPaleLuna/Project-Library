package org.example.sorting;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class EvenFieldSortStrategy<T extends Comparable<? super T>> implements Sort<T> {
    private final String fieldName;
    private final Sort<T> baseSortStrategy;
    private final ExecutorService executor;

    public EvenFieldSortStrategy(String fieldName, Sort<T> baseSortStrategy, ExecutorService executor) {
        this.fieldName = fieldName;
        this.baseSortStrategy = baseSortStrategy;
        this.executor = executor;
    }

    @Override
    public void sort(List<T> list) {
        sort(list, Comparator.naturalOrder());
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;

        try {
            // Шаг 1: Собираем ВСЕ чётные элементы и их индексы из всего списка
            List<IndexedElement<T>> allEvenElements = collectAllEvenElements(list);

            if (allEvenElements.isEmpty()) {
                return; // Нет чётных элементов - нечего сортировать
            }

            // Шаг 2: Сортируем ВСЕ чётные элементы вместе
            List<T> evenElementsOnly = extractElements(allEvenElements);
            baseSortStrategy.sort(evenElementsOnly, comparator);

            // Шаг 3: Обновляем отсортированные элементы в исходных позициях
            updateSortedElements(list, allEvenElements, evenElementsOnly);

        } catch (Exception e) {
            throw new RuntimeException("Error during even field sorting", e);
        }
    }

    private List<IndexedElement<T>> collectAllEvenElements(List<T> list) {
        try {
            // Используем многопоточность для сбора чётных элементов
            List<Callable<List<IndexedElement<T>>>> tasks = new ArrayList<>();

            int mid = list.size() / 2;
            List<T> firstHalf = list.subList(0, mid);
            List<T> secondHalf = list.subList(mid, list.size());

            // Задача для первой половины
            tasks.add(() -> collectEvenElementsFromSubList(firstHalf, 0));
            // Задача для второй половины
            tasks.add(() -> collectEvenElementsFromSubList(secondHalf, mid));

            // Выполняем сбор в ThreadPool
            List<Future<List<IndexedElement<T>>>> futures = executor.invokeAll(tasks);

            // Объединяем результаты
            List<IndexedElement<T>> allEvenElements = new ArrayList<>();
            for (Future<List<IndexedElement<T>>> future : futures) {
                allEvenElements.addAll(future.get());
            }

            return allEvenElements;

        } catch (Exception e) {
            throw new RuntimeException("Error collecting even elements", e);
        }
    }

    private List<IndexedElement<T>> collectEvenElementsFromSubList(List<T> subList, int startIndex) {
        List<IndexedElement<T>> evenElements = new ArrayList<>();

        for (int i = 0; i < subList.size(); i++) {
            T element = subList.get(i);
            if (isEvenField(element)) {
                evenElements.add(new IndexedElement<>(startIndex + i, element));
            }
        }

        return evenElements;
    }

    private List<T> extractElements(List<IndexedElement<T>> indexedElements) {
        List<T> elements = new ArrayList<>();
        for (IndexedElement<T> indexedElement : indexedElements) {
            elements.add(indexedElement.element);
        }
        return elements;
    }

    private void updateSortedElements(List<T> originalList,
                                      List<IndexedElement<T>> originalIndexedElements,
                                      List<T> sortedElements) {
        // Сортируем indexed elements по их индексам для корректного обновления
        List<IndexedElement<T>> sortedByIndex = new ArrayList<>(originalIndexedElements);
        sortedByIndex.sort(Comparator.comparingInt(ie -> ie.originalIndex));

        // Обновляем элементы в исходном списке
        for (int i = 0; i < sortedByIndex.size(); i++) {
            int originalIndex = sortedByIndex.get(i).originalIndex;
            originalList.set(originalIndex, sortedElements.get(i));
        }
    }

    private boolean isEvenField(T element) {
        try {
            java.lang.reflect.Field field = element.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(element);

            if (value instanceof Integer) {
                int intValue = (Integer) value;
                return intValue % 2 == 0;
            } else if (value instanceof Long) {
                long longValue = (Long) value;
                return longValue % 2 == 0;
            } else if (value instanceof Double) {
                double doubleValue = (Double) value;
                return ((long) doubleValue) % 2 == 0;
            } else if (value instanceof Float) {
                float floatValue = (Float) value;
                return ((long) floatValue) % 2 == 0;
            } else if (value instanceof Short) {
                short shortValue = (Short) value;
                return shortValue % 2 == 0;
            } else if (value instanceof Byte) {
                byte byteValue = (Byte) value;
                return byteValue % 2 == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
        return false;
    }

    private static class IndexedElement<T> {
        final int originalIndex;
        final T element;

        IndexedElement(int originalIndex, T element) {
            this.originalIndex = originalIndex;
            this.element = element;
        }
    }
}