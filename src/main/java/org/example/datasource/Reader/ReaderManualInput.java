package org.example.datasource.Reader;

import org.example.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReaderManualInput implements ReaderDataSource {

    @Override
    public List<Reader> generateReaders() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many readers do you want to enter? ");
        int count = getInt(scanner);
        if (count <= 0) {
            System.out.println("Count must be positive. Skipping.");
            return new ArrayList<>();
        }

        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Reader #" + (i + 1) + " ---");

            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Age: ");
            int age = getInt(scanner);

            System.out.print("Library card number: ");
            String libraryCardNumber = scanner.nextLine().trim();

            try {
                Reader reader = new Reader.Builder()
                        .name(name)
                        .age(age)
                        .libraryCardNumber(libraryCardNumber)
                        .build();
                readers.add(reader);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid reader: " + e.getMessage() + ". Skipping.");
                i--;
            }
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