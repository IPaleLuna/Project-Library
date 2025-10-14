package org.example.datasource.Book;

import org.example.collections.CustomCollection;
import org.example.model.Book;
import org.example.util.JsonLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookFileInput implements BookDataSource {

    @Override
    public CustomCollection<Book> generateBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file path (default: books.json): ");
        String filePath = scanner.nextLine().trim();
        if (filePath.isEmpty()) {
            filePath = "books.json";
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return new CustomCollection<>();
        }

        try {
            CustomCollection<Book> books = JsonLoader.loadList(file, new tools.jackson.core.type.TypeReference<CustomCollection<Book>>() {});
            System.out.println("Loaded " + books.size() + " books from '" + filePath + "'");
            return books;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return new CustomCollection<>();
        }
    }
}