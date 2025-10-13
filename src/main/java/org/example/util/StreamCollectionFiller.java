package org.example.util;

import org.example.collections.CustomCollection;
import org.example.interfaces.DataParser;
import org.example.interfaces.DataValidator;
import org.example.interfaces.RandomDataGenerator;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamCollectionFiller {

    public static <T> CustomCollection<T> fillFromFile(String filename,
                                                       DataValidator<T> validator,
                                                       DataParser<T> parser) {
        try {
            Stream<String> lines = java.nio.file.Files.lines(java.nio.file.Paths.get(filename));
            return lines.map(parser::parse)
                    .filter(validator::isValid)
                    .collect(Collectors.toCollection(CustomCollection::new));
        } catch (Exception e) {
            throw new RuntimeException("Error reading from file: " + filename, e);
        }
    }

    public static <T> CustomCollection<T> fillWithRandom(int size,
                                                         RandomDataGenerator<T> generator,
                                                         DataValidator<T> validator) {
        return Stream.generate(generator::generate)
                .filter(validator::isValid)
                .limit(size)
                .collect(Collectors.toCollection(CustomCollection::new));
    }

    public static <T> CustomCollection<T> fillManually(Scanner scanner,
                                                       int size,
                                                       DataParser<T> parser,
                                                       DataValidator<T> validator) {
        return Stream.iterate(0, i -> i + 1)
                .limit(size)
                .map(i -> {
                    System.out.println("Enter element " + (i + 1) + ":");
                    String input = scanner.nextLine();
                    return parser.parse(input);
                })
                .filter(validator::isValid)
                .collect(Collectors.toCollection(CustomCollection::new));
    }
}