package org.example.datasource.Book;

import org.example.collections.CustomCollection;
import org.example.model.Book;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        AtomicInteger counter = new AtomicInteger(1);

        CustomCollection<Book> books = Stream.generate(() -> readValidBook(scanner, counter.getAndIncrement()))
            .limit(count)
            .collect(Collectors.toCollection(CustomCollection::new));
        return books;
    }

    private Book readValidBook(Scanner scanner, int i) {
        while (true) {
            try {
                System.out.println("\n--- Book #" + i + " ---");


                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Page count: ");
                int pageCount = getInt(scanner);
                System.out.print("Publication year: ");
                int publicationYear = getInt(scanner);
            
                return new Book.Builder()
                    .name(name)
                    .pageCount(pageCount)
                    .publicationYear(publicationYear)
                    .build();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid book: " + e.getMessage() + ". Please try again.");
            }
        }
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