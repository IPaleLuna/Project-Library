package org.example.datasource.Reader;

import org.example.collections.CustomCollection;
import org.example.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReaderRandomInput implements ReaderDataSource {
    private static final String[] NAMES = {
            "Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince",
            "Ethan Hunt", "Fiona Gallagher", "George Martin", "Helen Keller"
    };
    private static final String[] CARD_PREFIXES = {"LIB-", "CARD-", "READER-"};
    private final Random random = new Random();

    @Override
    public CustomCollection<Reader> generateReaders() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many random readers to generate? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new CustomCollection<>();
        }

        CustomCollection<Reader> readers = Stream.generate(() -> 
        new Reader.Builder()
            .name(NAMES[random.nextInt(NAMES.length)])
            .age(random.nextInt(10, 90))
            .libraryCardNumber(CARD_PREFIXES[random.nextInt(CARD_PREFIXES.length)] + (1000 + random.nextInt(9000)))
            .build())
            .limit(count)
            .collect(Collectors.toCollection(CustomCollection::new));

        return readers;
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