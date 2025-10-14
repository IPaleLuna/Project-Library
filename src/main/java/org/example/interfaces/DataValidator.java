package org.example.interfaces;

public interface DataValidator<T> {
    boolean isValid(T data);
}