package org.example.app;

import org.example.collections.CustomCollection;
import org.example.datasource.Book.BookDataSource;
import org.example.datasource.Book.BookFileInput;
import org.example.datasource.Book.BookManualInput;
import org.example.datasource.Book.BookRandomInput;
import org.example.datasource.Reader.ReaderDataSource;
import org.example.datasource.Reader.ReaderFileInput;
import org.example.datasource.Reader.ReaderManualInput;
import org.example.datasource.Reader.ReaderRandomInput;
import org.example.model.Book;
import org.example.model.Reader;
import org.example.search.BinarySearch;
import org.example.search.MultithreadedSearch;
import org.example.sorting.ParallelMergeSort;
import org.example.sorting.ParallelQuickSort;
import org.example.sorting.ParallelTimSort;
import org.example.sorting.Sort;
import org.example.util.JsonLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Data Processor ===");
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> runBookProcessing();
                case "2" -> runReaderProcessing();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 0, 1, or 2.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Process Books");
        System.out.println("2. Process Readers");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private static void runBookProcessing() {
        System.out.println("\nSelect data source:");
        System.out.println("1. Manual input");
        System.out.println("2. Random generation");
        System.out.println("3. Load from file");
        System.out.print("Your choice: ");
        int sourceChoice = getIntInput();

        BookDataSource source = switch (sourceChoice) {
            case 1 -> new BookManualInput();
            case 2 -> new BookRandomInput();
            case 3 -> new BookFileInput();
            default -> {
                System.out.println("Invalid choice. Using random.");
                yield new BookRandomInput();
            }
        };

        CustomCollection<Book> books;
        try {
            books = source.generateBooks();
            if (books.isEmpty()) {
                System.out.println("No books to process.");
                return;
            }
            System.out.println("\nLoaded " + books.size() + " books:");
            books.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error loading books: " + e.getMessage());
            return;
        }

        saveCollectionToFile(books, "original books", "books_original.json");

        System.out.println("\n--- Sorting Options ---");
        System.out.println("1. QuickSort");
        System.out.println("2. MergeSort");
        System.out.println("3. TimSort");
        System.out.println("4. Skip sorting");
        System.out.print("Choose sorting algorithm: ");
        int sortChoice = getIntInput();

        if (sortChoice == 4) {
            System.out.println("Sorting skipped.");
            return;
        }

        System.out.println("\n--- Sort by ---");
        System.out.println("1. Name");
        System.out.println("2. Page Count");
        System.out.println("3. Publication Year");
        System.out.println("4. Default (name → pages → year)");
        System.out.print("Choose sort criterion: ");
        int criterionChoice = getIntInput();

        Comparator<Book> comparator = switch (criterionChoice) {
            case 1 -> Book.BY_NAME;
            case 2 -> Book.BY_PAGE_COUNT;
            case 3 -> Book.BY_PUBLICATION_YEAR;
            case 4 -> Comparator.naturalOrder();
            default -> {
                System.out.println("Invalid criterion. Using default.");
                yield Comparator.naturalOrder();
            }
        };

        Sort<Book> sortingStrategy = switch (sortChoice) {
            case 1 -> new ParallelQuickSort<Book>();
            case 2 -> new ParallelMergeSort<Book>();
            case 3 -> new ParallelTimSort<Book>();
            default -> throw new IllegalStateException("Unexpected sort choice");
        };

        try {
            System.out.println("\nSorting books...");
            long start = System.currentTimeMillis();
            sortingStrategy.sort(books, comparator);
            long end = System.currentTimeMillis();
            System.out.println("Sorted in " + (end - start) + " ms");
            System.out.println("\n Sorted books:");
            books.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Sorting failed: " + e.getMessage());
            e.printStackTrace();
        }

        saveCollectionToFile(books, "sorted books", "sorted_books.json");

        performBookBinarySearch(books, comparator);

        performBookMultithreadedSearch(books);


    }

    private static void runReaderProcessing() {
        System.out.println("\nSelect data source:");
        System.out.println("1. Manual input");
        System.out.println("2. Random generation");
        System.out.println("3. Load from file");
        System.out.print("Your choice: ");
        int sourceChoice = getIntInput();

        ReaderDataSource source = switch (sourceChoice) {
            case 1 -> new ReaderManualInput();
            case 2 -> new ReaderRandomInput();
            case 3 -> new ReaderFileInput();
            default -> {
                System.out.println("Invalid choice. Using random.");
                yield new ReaderRandomInput();
            }
        };

        List<Reader> readers;
        try {
            readers = source.generateReaders();
            if (readers.isEmpty()) {
                System.out.println("No readers to process.");
                return;
            }
            System.out.println("\nLoaded " + readers.size() + " readers:");
            readers.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error loading readers: " + e.getMessage());
            return;
        }

        saveCollectionToFile(readers, "original readers", "original_readers.json");

        System.out.println("\n--- Sorting Options ---");
        System.out.println("1. QuickSort");
        System.out.println("2. MergeSort");
        System.out.println("3. TimSort");
        System.out.println("4. Skip sorting");
        System.out.print("Choose sorting algorithm: ");
        int sortChoice = getIntInput();

        if (sortChoice == 4) {
            System.out.println("Sorting skipped.");
            return;
        }

        System.out.println("\n--- Sort by ---");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Library Card");
        System.out.println("4. Default (name → age → card)");
        System.out.print("Choose sort criterion: ");
        int criterionChoice = getIntInput();

        Comparator<Reader> comparator = switch (criterionChoice) {
            case 1 -> Reader.BY_NAME;
            case 2 -> Reader.BY_AGE;
            case 3 -> Reader.BY_LIBRARY_CARD;
            case 4 -> Comparator.naturalOrder();
            default -> {
                System.out.println("Invalid criterion. Using default.");
                yield Comparator.naturalOrder();
            }
        };

        Sort<Reader> sortingStrategy = switch (sortChoice) {
            case 1 -> new ParallelQuickSort<Reader>();
            case 2 -> new ParallelMergeSort<Reader>();
            case 3 -> new ParallelTimSort<Reader>();
            default -> throw new IllegalStateException("Unexpected sort choice");
        };

        try {
            System.out.println("\nSorting readers...");
            sortingStrategy.sort(readers, comparator);
            System.out.println("Sorted readers:");
            readers.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Sorting failed: " + e.getMessage());
        }
        saveCollectionToFile(readers, "sorted readers", "sorter_readers.json");

        performReaderBinarySearch(readers, comparator);

        performReaderMultithreadedSearch(readers);

    }

    private static <T> void saveCollectionToFile(List<T> data, String entityType, String defaultFilename) {
        System.out.print("\nSave " + entityType + " to file? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            return;
        }
        System.out.print("Enter filename (default: " + defaultFilename + "): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = defaultFilename;
        }
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }
        try {
            JsonLoader.save(new File(filename), data);
            System.out.println(entityType + " saved to " + filename);
        } catch (Exception e) {
            System.err.println("Failed to save " + entityType + ": " + e.getMessage());
        }
    }

    private static void performBookBinarySearch(CustomCollection<Book> books, Comparator<Book> sortComparator) {
        if (sortComparator == Book.BY_NAME || sortComparator == Comparator.naturalOrder()) {
            System.out.print("Enter book name to search: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }
            Book target = new Book.Builder()
                    .name(name)
                    .pageCount(1)
                    .publicationYear(1600)
                    .build();
            int index = BinarySearch.searchBy(books, target, Book.BY_NAME);
            if (index >= 0) {
                System.out.println("Found: " + books.get(index));
                List<Book> result = new ArrayList<>();
                result.add(books.get(index));
                saveCollectionToFile(result, "book search result", "book_search_result.json");
            } else {
                System.out.println("Book not found.");
            }
        } else if (sortComparator == Book.BY_PUBLICATION_YEAR) {
            System.out.print("Enter publication year: ");
            int year = getIntInput();
            if (year < 1600 || year > java.time.Year.now().getValue() + 1) {
                System.out.println("Year must be between 1600 and next year.");
                return;
            }
            Book target = new Book.Builder()
                    .name("DUMMY")
                    .pageCount(1)
                    .publicationYear(year)
                    .build();
            int index = BinarySearch.searchBy(books, target, Book.BY_PUBLICATION_YEAR);
            if (index >= 0) {
                System.out.println("Found: " + books.get(index));
                List<Book> result = new ArrayList<>();
                result.add(books.get(index));
                saveCollectionToFile(result, "book search result", "book_search_result.json");
            } else {
                System.out.println("Book not found.");
            }
        } else if (sortComparator == Book.BY_PAGE_COUNT) {
            System.out.print("Enter page count: ");
            int pages = getIntInput();
            if (pages <= 0) {
                System.out.println("Page count must be positive.");
                return;
            }
            Book target = new Book.Builder()
                    .name("DUMMY")
                    .pageCount(pages)
                    .publicationYear(1600)
                    .build();
            int index = BinarySearch.searchBy(books, target, Book.BY_PAGE_COUNT);
            if (index >= 0) {
                System.out.println("Found: " + books.get(index));
                List<Book> result = new ArrayList<>();
                result.add(books.get(index));
                saveCollectionToFile(result, "book search result", "book_search_result.json");
            } else {
                System.out.println("Book not found.");
            }
        } else {
            System.out.println("Binary search is only available when sorted by name, year, or page count.");
        }
    }

    private static void performBookMultithreadedSearch(CustomCollection<Book> books) {
        System.out.println("Count occurrences by:");
        System.out.println("1. Name");
        System.out.println("2. Publication Year");
        System.out.println("3. Page Count");
        System.out.print("Your choice: ");
        int choice = getIntInput();

        Book target;
        Comparator<Book> countComparator;

        switch (choice) {
            case 1 -> {
                System.out.print("Enter book name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = "DUMMY";
                target = new Book.Builder()
                        .name(name)
                        .pageCount(1)
                        .publicationYear(1600)
                        .build();
                countComparator = Book.BY_NAME;
            }
            case 2 -> {
                System.out.print("Enter publication year: ");
                int year = getIntInput();
                if (year < 1600 || year > java.time.Year.now().getValue() + 1) year = 1600;
                target = new Book.Builder()
                        .name("DUMMY")
                        .pageCount(1)
                        .publicationYear(year)
                        .build();
                countComparator = Book.BY_PUBLICATION_YEAR;
            }
            case 3 -> {
                System.out.print("Enter page count: ");
                int pages = getIntInput();
                if (pages <= 0) pages = 1;
                target = new Book.Builder()
                        .name("DUMMY")
                        .pageCount(pages)
                        .publicationYear(1600)
                        .build();
                countComparator = Book.BY_PAGE_COUNT;
            }
            default -> {
                System.out.println("Invalid choice. Using name.");
                System.out.print("Enter book name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = "DUMMY";
                target = new Book.Builder()
                        .name(name)
                        .pageCount(1)
                        .publicationYear(1600)
                        .build();
                countComparator = Book.BY_NAME;
            }
        }

        System.out.print("Enter number of threads (default 2): ");
        int threadCount = getIntInput();
        if (threadCount <= 0) threadCount = 2;

        MultithreadedSearch.countOccurrences(books, target, countComparator, threadCount);

        List<Book> matches = new ArrayList<>();
        for (Book book : books) {
            if (countComparator.compare(book, target) == 0) {
                matches.add(book);
            }
        }

        if (!matches.isEmpty()) {
            System.out.println("Found " + matches.size() + " matching books.");
            saveCollectionToFile(matches, "book search matches", "book_search_matches.json");
        } else {
            System.out.println("No matches found.");
        }
    }


    private static void performReaderBinarySearch(List<Reader> readers, Comparator<Reader> sortComparator) {
        if (sortComparator == Reader.BY_NAME || sortComparator == Comparator.naturalOrder()) {
            System.out.print("Enter reader name to search: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }
            Reader target = new Reader.Builder()
                    .name(name)
                    .age(0)
                    .libraryCardNumber("DUMMY-CARD")
                    .build();
            int index = BinarySearch.searchBy(readers, target, Reader.BY_NAME);
            if (index >= 0) {
                System.out.println("Found: " + readers.get(index));
                List<Reader> result = new ArrayList<>();
                result.add(readers.get(index));
                saveCollectionToFile(result, "reader search result", "reader_search_result.json");
            } else {
                System.out.println("Reader not found.");
            }
        } else if (sortComparator == Reader.BY_AGE) {
            System.out.print("Enter age: ");
            int age = getIntInput();
            if (age < 0 || age > 150) {
                System.out.println("Age must be between 0 and 150.");
                return;
            }
            Reader target = new Reader.Builder()
                    .name("DUMMY")
                    .age(age)
                    .libraryCardNumber("DUMMY-CARD")
                    .build();
            int index = BinarySearch.searchBy(readers, target, Reader.BY_AGE);
            if (index >= 0) {
                System.out.println("Found: " + readers.get(index));
                List<Reader> result = new ArrayList<>();
                result.add(readers.get(index));
                saveCollectionToFile(result, "reader search result", "reader_search_result.json");
            } else {
                System.out.println("Reader not found.");
            }
        } else if (sortComparator == Reader.BY_LIBRARY_CARD) {
            System.out.print("Enter library card number: ");
            String card = scanner.nextLine().trim();
            if (card.isEmpty()) {
                System.out.println("Library card number cannot be empty.");
                return;
            }
            Reader target = new Reader.Builder()
                    .name("DUMMY")
                    .age(0)
                    .libraryCardNumber(card)
                    .build();
            int index = BinarySearch.searchBy(readers, target, Reader.BY_LIBRARY_CARD);
            if (index >= 0) {
                System.out.println("Found: " + readers.get(index));
                List<Reader> result = new ArrayList<>();
                result.add(readers.get(index));
                saveCollectionToFile(result, "reader search result", "reader_search_result.json");
            } else {
                System.out.println("Reader not found.");
            }
        } else {
            System.out.println("Binary search is only available when sorted by name, age, or library card.");
        }
    }


    private static void performReaderMultithreadedSearch(List<Reader> readers) {
        System.out.println("Count occurrences by:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Library Card");
        System.out.print("Your choice: ");
        int choice = getIntInput();

        Reader target;
        Comparator<Reader> countComparator;

        switch (choice) {
            case 1 -> {
                System.out.print("Enter reader name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = "DUMMY";
                target = new Reader.Builder()
                        .name(name)
                        .age(0)
                        .libraryCardNumber("DUMMY-CARD")
                        .build();
                countComparator = Reader.BY_NAME;
            }
            case 2 -> {
                System.out.print("Enter age: ");
                int age = getIntInput();
                if (age < 0 || age > 150) age = 0;
                target = new Reader.Builder()
                        .name("DUMMY")
                        .age(age)
                        .libraryCardNumber("DUMMY-CARD")
                        .build();
                countComparator = Reader.BY_AGE;
            }
            case 3 -> {
                System.out.print("Enter library card number: ");
                String card = scanner.nextLine().trim();
                if (card.isEmpty()) card = "DUMMY-CARD";
                target = new Reader.Builder()
                        .name("DUMMY")
                        .age(0)
                        .libraryCardNumber(card)
                        .build();
                countComparator = Reader.BY_LIBRARY_CARD;
            }
            default -> {
                System.out.println("Invalid choice. Using name.");
                System.out.print("Enter reader name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) name = "DUMMY";
                target = new Reader.Builder()
                        .name(name)
                        .age(0)
                        .libraryCardNumber("DUMMY-CARD")
                        .build();
                countComparator = Reader.BY_NAME;
            }
        }

        System.out.print("Enter number of threads (default 2): ");
        int threadCount = getIntInput();
        if (threadCount <= 0) threadCount = 2;

        MultithreadedSearch.countOccurrences(readers, target, countComparator, threadCount);

        List<Reader> matches = new ArrayList<>();
        for (Reader reader : readers) {
            if (countComparator.compare(reader, target) == 0) {
                matches.add(reader);
            }
        }

        if (!matches.isEmpty()) {
            System.out.println("Found " + matches.size() + " matching readers.");
            saveCollectionToFile(matches, "reader search matches", "reader_search_matches.json");
        } else {
            System.out.println("No matches found.");
        }
    }


    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }
}