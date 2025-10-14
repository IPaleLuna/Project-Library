package org.example.interfaces;

public interface DataParser<T> {
    T parse(String data);
}