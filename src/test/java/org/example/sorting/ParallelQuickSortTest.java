package org.example.sorting;

import org.example.model.Book;
import org.example.model.Reader;
import org.junit.jupiter.api.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import org.example.util.JsonLoader;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests class ParallelQuickSort")
public class ParallelQuickSortTest {
    private final static File booksFile = new File("src/test/resources/books.json");
    private final static File readersFile = new File("src/test/resources/readers.json");

    @Test
    void testSortBooksByPageCount() throws IOException {
        List<Book> books = JsonLoader.loadList(booksFile, new TypeReference<>() {});

        ParallelQuickSort<Book> bookSorter = new ParallelQuickSort<>();
        bookSorter.sort(books, Book.BY_PAGE_COUNT);

        for (int i = 1; i < books.size(); i++) {
            assertTrue(books.get(i - 1).getPageCount() <= books.get(i).getPageCount());
        }
    }

    @Test
    void testSortBooksByName() throws IOException {
        List<Book> books = JsonLoader.loadList(booksFile, new TypeReference<>() {});
        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort(Book.BY_NAME);

        ParallelQuickSort<Book> bookSorter = new ParallelQuickSort<>();
        bookSorter.sort(books, Book.BY_NAME);

        assertEquals(books, sortedBooks);
    }

    @Test
    void testSortBooksByPublicationYear() throws IOException {
        List<Book> books = JsonLoader.loadList(booksFile, new TypeReference<>() {});

        ParallelQuickSort<Book> bookSorter = new ParallelQuickSort<>();
        bookSorter.sort(books, Book.BY_PUBLICATION_YEAR);

        for (int i = 1; i < books.size(); i++) {
            assertTrue(books.get(i - 1).getPublicationYear() <= books.get(i).getPublicationYear());
        }
    }

    @Test
    void testSortReadersByAge() throws IOException {
        List<Reader> readers = JsonLoader.loadList(readersFile, new TypeReference<>() {});

        ParallelQuickSort<Reader> readerSorter = new ParallelQuickSort<>();
        readerSorter.sort(readers, Reader.BY_AGE);

        for (int i = 1; i < readers.size(); i++) {
            assertTrue(readers.get(i - 1).getAge() <= readers.get(i).getAge());
        }
    }

    @Test
    void testSortReadersByName() throws IOException {
        List<Reader> readers = JsonLoader.loadList(readersFile, new TypeReference<>() {});
        List<Reader> sortedReaders = new ArrayList<>(readers);
        sortedReaders.sort(Reader.BY_NAME);

        ParallelQuickSort<Reader> readerSorter = new ParallelQuickSort<>();
        readerSorter.sort(readers, Reader.BY_NAME);

        assertEquals(readers, sortedReaders);
    }

    @Test
    void testSortReadersByLibraryCardNumber() throws IOException {
        List<Reader> readers = JsonLoader.loadList(readersFile, new TypeReference<>() {});
        List<Reader> sortedReaders = new ArrayList<>(readers);
        sortedReaders.sort(Reader.BY_LIBRARY_CARD);

        ParallelQuickSort<Reader> readerSorter = new ParallelQuickSort<>();
        readerSorter.sort(readers, Reader.BY_LIBRARY_CARD);

        assertEquals(readers, sortedReaders);
    }
}
