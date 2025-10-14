package org.example.datasource.Book;

import org.example.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BookRandomInput implements BookDataSource {
    private static final String[] TITLES = {
            "Echoes of Tomorrow", "Silent Horizons", "The Last Ember", "Shadows of the Mind",
            "Crimson Skies", "Fragments of Infinity", "Whispers in the Fog", "Beneath the Iron Sun"
    };
    private final Random random = new Random();

    @Override
    public List<Book> generateBooks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many random books to generate? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new ArrayList<>();
        }

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = TITLES[random.nextInt(TITLES.length)];
            int pageCount = random.nextInt(100, 1001);
            int year = random.nextInt(1600, 2026);

            books.add(new Book.Builder()
                    .name(name)
                    .pageCount(pageCount)
                    .publicationYear(year)
                    .build());
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