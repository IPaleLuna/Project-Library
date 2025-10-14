<<<<<<< HEAD
<<<<<<< HEAD
package org.example.app;

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
import org.example.sorting.ParallelMergeSort;
import org.example.sorting.ParallelQuickSort;
import org.example.sorting.ParallelTimSort;
import org.example.sorting.Sort;

import java.util.Comparator;
import java.util.List;
=======
>>>>>>> b953a2a78d2472f980f692763ad3fcd996ad6e35
=======
package org.example.app;

>>>>>>> origin/Dev
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Data Processor ===");
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
<<<<<<< HEAD
                case "1" -> runBookProcessing();
                case "2" -> runReaderProcessing();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 0, 1, or 2.");
=======
                case "1" -> {
                    System.out.println("Working with Books...");
                    //заглушка, сюда потом сортировку/ввод вывод и всё остальное
                    break;
                }
                case "2" -> {
                    System.out.println("Working with Reader...");
                    //тож заглушка
                    break;
                }
                case "0" -> {
                    System.out.println("Terminated");
                    return;
                }
                default -> System.out.println("Incorrect, only 1,2 or 0");
>>>>>>> b953a2a78d2472f980f692763ad3fcd996ad6e35
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
<<<<<<< HEAD
<<<<<<< HEAD

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

        List<Book> books;
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
=======
>>>>>>> b953a2a78d2472f980f692763ad3fcd996ad6e35
}
=======
}
>>>>>>> origin/Dev
