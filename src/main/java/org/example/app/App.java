package main.java.org.example.app;

import java.util.Comparator;
import java.util.List;

import main.java.org.example.PaleLuna.Book;
import main.java.org.example.PaleLuna.Reader;
import main.java.org.example.search.BinarySearch;

public class App {
    public static void main(String[] args) throws Exception {
        // Создание и сортировка коллекций
        List<Book> books = List.of(
            new Book("Metro 2033", "Dmitry Glukhovsky", 2005),
            new Book("Fallout: Equestria", "Kkat", 2011),
            new Book("The Master and Margarita", "Mikhail Bulgakov", 1967),
            new Book("War and Peace", "Leo Tolstoy", 1869)
        );

        List<Reader> readers = List.of(
            new Reader("Alice", 101, "alice@example.com"),
            new Reader("Bob", 102, "bob@example.com"),
            new Reader("Charlie", 103, "charlie@example.com")
        );

        // Компараторы для разных полей
        Comparator<Book> bookTitleComparator = Comparator.comparing(Book::getTitle);
        Comparator<Reader> readerIdComparator = Comparator.comparing(Reader::getId);

        // Поиск книги по названию
        Book keyBook = new Book("War and Peace", "", 0);
        int bookIndex = BinarySearch.searchBy(books, keyBook, bookTitleComparator);
        System.out.println("Found book at index: " + bookIndex);

        // Поиск читателя по ID
        Reader keyReader = new Reader("", 103, "");
        int readerIndex = BinarySearch.searchBy(readers, keyReader, readerIdComparator);
        System.out.println("Found reader at index: " + readerIndex);
    }
}
