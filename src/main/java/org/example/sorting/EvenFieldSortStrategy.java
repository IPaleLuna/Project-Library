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
        // Используем natural ordering
        sort(list, Comparator.naturalOrder());
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        try {
            // Создаем задачи для многопоточной сортировки
            List<Callable<Void>> tasks = new ArrayList<>();

            // Разделяем список на две части для обработки в разных потоках
            int mid = list.size() / 2;
            List<T> firstHalf = new ArrayList<>(list.subList(0, mid));
            List<T> secondHalf = new ArrayList<>(list.subList(mid, list.size()));

            // Задача для первой половины
            tasks.add(() -> {
                processHalf(firstHalf, comparator);
                return null;
            });

            // Задача для второй половины
            tasks.add(() -> {
                processHalf(secondHalf, comparator);
                return null;
            });

            // Выполняем задачи в ThreadPool
            List<Future<Void>> futures = executor.invokeAll(tasks);

            // Ждем завершения всех задач
            for (Future<Void> future : futures) {
                future.get();
            }

            // Объединяем результаты
            list.clear();
            list.addAll(firstHalf);
            list.addAll(secondHalf);

        } catch (Exception e) {
            throw new RuntimeException("Error during even field sorting", e);
        }
    }

    private void processHalf(List<T> halfList, Comparator<T> comparator) {
        // Собираем элементы с чётными значениями поля
        List<T> evenElements = new ArrayList<>();
        List<Integer> evenIndices = new ArrayList<>();

        for (int i = 0; i < halfList.size(); i++) {
            T element = halfList.get(i);
            if (isEvenField(element)) {
                evenElements.add(element);
                evenIndices.add(i);
            }
        }

        // Сортируем элементы с чётными значениями
        if (!evenElements.isEmpty()) {
            baseSortStrategy.sort(evenElements, comparator);

            // Заменяем отсортированные элементы в исходных позициях
            for (int i = 0; i < evenIndices.size(); i++) {
                int index = evenIndices.get(i);
                halfList.set(index, evenElements.get(i));
            }
        }
    }

    private boolean isEvenField(T element) {
        try {
            // Получаем значение числового поля через рефлексию
            java.lang.reflect.Field field = element.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(element);

            if (value instanceof Integer) {
                return ((Integer) value) % 2 == 0;
            } else if (value instanceof Long) {
                return ((Long) value) % 2 == 0;
            } else if (value instanceof Double) {
                // Для double проверяем, является ли целая часть чётной
                double doubleValue = (Double) value;
                return ((long) doubleValue) % 2 == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
        return false;
    }
}
