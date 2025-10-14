package org.example.collections;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CustomCollection<T> implements List<T> {
    private final List<T> data;

    public CustomCollection() {
        this.data = new ArrayList<>();
    }

    public CustomCollection(Collection<? extends T> collection) {
        this.data = new ArrayList<>(Objects.requireNonNull(collection, "Collection cannot be null"));
    }

    // Кастомные методы
    public CustomCollection<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        return this.data.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(CustomCollection::new));
    }

    public <R> CustomCollection<R> map(java.util.function.Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "Mapper function cannot be null");
        return this.data.stream()
                .map(mapper)
                .collect(Collectors.toCollection(CustomCollection::new));
    }

    public void forEachWithIndex(BiConsumer<T, Integer> action) {
        Objects.requireNonNull(action, "Action cannot be null");
        for (int i = 0; i < data.size(); i++) {
            action.accept(data.get(i), i);
        }
    }

    public OptionalDouble getAverage(String fieldName) {
        Objects.requireNonNull(fieldName, "Field name cannot be null");
        return data.stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> getNumericFieldValue(item, fieldName))
                .average();
    }

    private double getNumericFieldValue(T item, String fieldName) {
        try {
            java.lang.reflect.Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(item);

            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + fieldName, e);
        }
        return 0.0;
    }

    // Реализация методов List interface с проверками
    @Override
    public T get(int index) {
        checkIndex(index);
        return data.get(index);
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);
        Objects.requireNonNull(element, "Element cannot be null");
        return data.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        checkIndexForAdd(index);
        Objects.requireNonNull(element, "Element cannot be null");
        data.add(index, element);
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        return data.remove(index);
    }

    @Override public int indexOf(Object o) { return data.indexOf(o); }
    @Override public int lastIndexOf(Object o) { return data.lastIndexOf(o); }
    @Override public ListIterator<T> listIterator() { return data.listIterator(); }

    @Override
    public ListIterator<T> listIterator(int index) {
        checkIndex(index);
        return data.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex - 1); // toIndex exclusive
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex > toIndex");
        }
        return data.subList(fromIndex, toIndex);
    }

    // Оптимизированная реализация containsAll
    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c, "Collection cannot be null");

        // Оптимизация для небольших коллекций - используем стандартную реализацию
        if (c.size() > 10) {
            // Для больших коллекций создаем HashSet для быстрого поиска
            Set<?> checkSet = new HashSet<>(c);
            for (Object element : checkSet) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        } else {
            // Для небольших коллекций используем стандартный подход
            for (Object element : c) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Objects.requireNonNull(c, "Collection cannot be null");
        return data.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "Collection cannot be null");
        return data.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c, "Collection cannot be null");
        return data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c, "Collection cannot be null");
        return data.retainAll(c);
    }

    // Остальные методы Collection interface

    @Override
    public <T1> T1[] toArray(T1[] a) {
        Objects.requireNonNull(a, "Array cannot be null");
        return data.toArray(a);
    }

    @Override
    public boolean add(T t) {
        Objects.requireNonNull(t, "Element cannot be null");
        return data.add(t);
    }

    // Вспомогательные методы для проверки индексов
    private void checkIndex(int index) {
        if (index < 0 || index >= data.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + data.size());
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > data.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + data.size());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof List)) return false;
        return data.equals(o);
    }

    @Override public boolean remove(Object o) { return data.remove(o); }
    @Override public void clear() { data.clear(); }
    @Override public String toString() { return data.toString(); }
    @Override public int size() { return data.size(); }
    @Override public boolean isEmpty() { return data.isEmpty(); }
    @Override public boolean contains(Object o) { return data.contains(o); }
    @Override public Iterator<T> iterator() { return data.iterator(); }
    @Override public Object[] toArray() { return data.toArray(); }
    @Override public int hashCode() { return data.hashCode(); }

    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }
}