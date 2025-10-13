package org.example.collections;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CustomCollection<T> implements Collection<T> {
    private final List<T> data;

    public CustomCollection() {
        this.data = new ArrayList<>();
    }

    public CustomCollection(Collection<? extends T> collection) {
        this.data = new ArrayList<>(collection);
    }

    public CustomCollection<T> filter(Predicate<T> predicate) {
        return this.data.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(CustomCollection::new));
    }

    public <R> CustomCollection<R> map(java.util.function.Function<T, R> mapper) {
        return this.data.stream()
                .map(mapper)
                .collect(Collectors.toCollection(CustomCollection::new));
    }

    public void forEachWithIndex(BiConsumer<T, Integer> action) {
        for (int i = 0; i < data.size(); i++) {
            action.accept(data.get(i), i);
        }
    }

    public OptionalDouble getAverage(String fieldName) {
        return data.stream()
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

    @Override public int size() { return data.size(); }
    @Override public boolean isEmpty() { return data.isEmpty(); }
    @Override public boolean contains(Object o) { return data.contains(o); }
    @Override public Iterator<T> iterator() { return data.iterator(); }
    @Override public Object[] toArray() { return data.toArray(); }
    @Override public <T1> T1[] toArray(T1[] a) { return data.toArray(a); }
    @Override public boolean add(T t) { return data.add(t); }
    @Override public boolean remove(Object o) { return data.remove(o); }
    @Override public boolean containsAll(Collection<?> c) { return data.containsAll(c); }
    @Override public boolean addAll(Collection<? extends T> c) { return data.addAll(c); }
    @Override public boolean removeAll(Collection<?> c) { return data.removeAll(c); }
    @Override public boolean retainAll(Collection<?> c) { return data.retainAll(c); }
    @Override public void clear() { data.clear(); }

    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }
}