package org.example.datasource.Reader;

import org.example.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ReaderRandomInput implements ReaderDataSource {
    private static final String[] NAMES = {
            "Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince",
            "Ethan Hunt", "Fiona Gallagher", "George Martin", "Helen Keller"
    };
    private static final String[] CARD_PREFIXES = {"LIB-", "CARD-", "READER-"};
    private final Random random = new Random();

    @Override
    public List<Reader> generateReaders() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many random readers to generate? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new ArrayList<>();
        }

        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = NAMES[random.nextInt(NAMES.length)];
            int age = random.nextInt(10, 90);
            String prefix = CARD_PREFIXES[random.nextInt(CARD_PREFIXES.length)];
            String libraryCardNumber = prefix + (1000 + random.nextInt(9000));

            readers.add(new Reader.Builder()
                    .name(name)
                    .age(age)
                    .libraryCardNumber(libraryCardNumber)
                    .build());
        }
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