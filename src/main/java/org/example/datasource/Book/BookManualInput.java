package org.example.datasource.Book;

import org.example.collections.CustomCollection;
import org.example.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookManualInput implements BookDataSource {

    @Override
    public CustomCollection<Book> generateBooks() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many books do you want to enter? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new CustomCollection<>();
        }

        CustomCollection<Book> books = new CustomCollection<>();
        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Book #" + (i + 1) + " ---");

            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Page count: ");
            int pageCount = getInt(scanner);

            System.out.print("Publication year: ");
            int publicationYear = getInt(scanner);

            try {
                Book book = new Book.Builder()
                        .name(name)
                        .pageCount(pageCount)
                        .publicationYear(publicationYear)
                        .build();
                books.add(book);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid book: " + e.getMessage() + ". Skipping.");
                i--;
            }
        }
        return books;
    }

    private int getInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}