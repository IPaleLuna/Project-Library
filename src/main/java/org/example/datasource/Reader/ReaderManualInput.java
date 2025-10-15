package org.example.datasource.Reader;

import org.example.collections.CustomCollection;
import org.example.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReaderManualInput implements ReaderDataSource {

    @Override
    public CustomCollection<Reader> generateReaders() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many readers do you want to enter? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new CustomCollection<>();
        }

        AtomicInteger counter = new AtomicInteger(1);
        CustomCollection<Reader> readers = Stream.generate(() -> readValidReader(scanner, counter.getAndIncrement()))
            .limit(count)
            .collect(Collectors.toCollection(CustomCollection::new));
            
        return readers;
    }

    private Reader readValidReader(Scanner scanner, int i)
    {
        while (true) {
            try {
                System.out.println("\n--- Reader #" + i + " ---");

                System.out.print("Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Age: ");
                int age = getInt(scanner);

                System.out.print("Library card number: ");
                String libraryCardNumber = scanner.nextLine().trim();

                return new Reader.Builder()
                        .name(name)
                        .age(age)
                        .libraryCardNumber(libraryCardNumber)
                        .build();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid reader: " + e.getMessage() + ". Please try again.");
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