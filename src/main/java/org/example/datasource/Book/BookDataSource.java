package org.example.datasource.Book;

import org.example.model.Book;

import java.util.List;
import java.util.Scanner;

public interface BookDataSource {
    List<Book> generateBooks();
}
