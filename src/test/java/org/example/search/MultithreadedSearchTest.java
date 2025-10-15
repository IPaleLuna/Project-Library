package org.example.search;

import org.example.model.Book;
import org.example.util.JsonLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MultithreadedSearchTest {
    
    private List<Book> books;
    private final static File booksFile = new File("src/test/resources/books.json");
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        books = JsonLoader.loadList(booksFile, new TypeReference<List<Book>>(){});
        
        // Перенаправляем System.out для проверки вывода
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testCountOccurrencesByName() {
        Book targetBook = new Book.Builder()
                .name("Echoes of Tomorrow")
                .pageCount(245)
                .publicationYear(2001)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_NAME, 4);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Общее количество вхождений элемента: 1"));
    }

    @Test
    void testCountOccurrencesByPublicationYear() {
        Book targetBook = new Book.Builder()
                .name("Any")
                .pageCount(12)
                .publicationYear(2018)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_PUBLICATION_YEAR, 3);
        
        String output = outputStream.toString();

        assertTrue(output.contains("Общее количество вхождений элемента: 3"));
    }

    @Test
    void testCountOccurrencesByPageCount() {
        Book targetBook = new Book.Builder()
                .name("Any")
                .pageCount(490)
                .publicationYear(2000)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_PAGE_COUNT, 2);
        
        String output = outputStream.toString();

        assertTrue(output.contains("Общее количество вхождений элемента: 2"));
    }

    @Test
    void testCountOccurrencesWithDifferentThreadCounts() {
        Book targetBook = new Book.Builder()
                .name("The Silent Voyage")
                .pageCount(280)
                .publicationYear(2022)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_NAME, 1);
        String output1 = outputStream.toString();
        assertTrue(output1.contains("Общее количество вхождений элемента: 1"));
        
        outputStream.reset();
        
        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_NAME, 8);
        String output2 = outputStream.toString();
        assertTrue(output2.contains("Общее количество вхождений элемента: 1"));
    }

    @Test
    void testCountOccurrencesNotFound() {
        Book nonExistingBook = new Book.Builder()
                .name("Non Existing Book")
                .pageCount(999)
                .publicationYear(2000)
                .build();

        MultithreadedSearch.countOccurrences(books, nonExistingBook, Book.BY_NAME, 4);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Общее количество вхождений элемента: 0"));
    }

    @Test
    void testCountOccurrencesWithEmptyList() {
        List<Book> emptyList = List.of();
        Book targetBook = new Book.Builder()
                .name("Test")
                .pageCount(100)
                .publicationYear(2020)
                .build();

        MultithreadedSearch.countOccurrences(emptyList, targetBook, Book.BY_NAME, 4);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Коллекция пуста. Вхождений: 0"));
    }

    @Test
    void testCountOccurrencesWithSingleThread() {
        Book targetBook = new Book.Builder()
                .name("Crimson Skies")
                .pageCount(390)
                .publicationYear(2005)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_NAME, 1);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Общее количество вхождений элемента: 1"));
        assertTrue(output.contains("обработал индексы")); // Проверяем вывод одного потока
    }

    @Test
    void testCountOccurrencesWithMultipleMatches() {
        Book targetBook = new Book.Builder()
                .name("Any")
                .pageCount(1)
                .publicationYear(2019)
                .build();

        MultithreadedSearch.countOccurrences(books, targetBook, Book.BY_PUBLICATION_YEAR, 4);
        
        String output = outputStream.toString();

        assertTrue(output.contains("Общее количество вхождений элемента: 4"));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Восстанавливаем оригинальный System.out
        System.setOut(originalOut);
    }
}