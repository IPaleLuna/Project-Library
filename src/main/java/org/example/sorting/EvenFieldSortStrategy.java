package org.example.sorting;

import org.example.interfaces.SortStrategy;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


// Стратегия сортировки для чётных значений поля
public class EvenFieldSortStrategy<T> implements SortStrategy<T> {
    private final String fieldName;
    private final SortStrategy<T> baseSortStrategy;
    private final ExecutorService executor;

    public EvenFieldSortStrategy(String fieldName, SortStrategy<T> baseSortStrategy, ExecutorService executor) {
        this.fieldName = fieldName;
        this.baseSortStrategy = baseSortStrategy;
        this.executor = executor;
    }

    @Override
    public void sort(List<T> list) {
        try {
            // Создаем задачи для многопоточной сортировки
            List<Callable<Void>> tasks = new ArrayList<>();

            // Разделяем список на две части для обработки в разных потоках
            int mid = list.size() / 2;
            List<T> firstHalf = new ArrayList<>(list.subList(0, mid));
            List<T> secondHalf = new ArrayList<>(list.subList(mid, list.size()));

            // Задача для первой половины
            tasks.add(() -> {
                processHalf(firstHalf);
                return null;
            });

            // Задача для второй половины
            tasks.add(() -> {
                processHalf(secondHalf);
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

    private void processHalf(List<T> halfList) {
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
            baseSortStrategy.sort(evenElements);

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
                return ((Double) value) % 2 == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
        return false;
    }
}
