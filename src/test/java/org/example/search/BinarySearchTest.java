package org.example.search;

import org.example.model.Book;
import org.example.util.JsonLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {
    private final static File booksFile = new File("src/test/resources/books.json");
    private List<Book> books;

    @BeforeEach
    void setUp() throws IOException {
        books = JsonLoader.loadList(booksFile, new TypeReference<List<Book>>(){});
    }

    @Test
    void testSearchByName() {
        // Сортируем по имени для корректного бинарного поиска
        books.sort(Book.BY_NAME);
        
        Book existingBook = new Book.Builder()
                .name("Crimson Skies")
                .pageCount(390)
                .publicationYear(2005)
                .build();

        int index = BinarySearch.searchBy(books, existingBook, Book.BY_NAME);
        assertEquals(6, index);

        Book nonExistingBook = new Book.Builder()
                .name("Zeta")
                .pageCount(390)
                .publicationYear(2005)
                .build();

        int notFoundIndex = BinarySearch.searchBy(books, nonExistingBook, Book.BY_NAME);
        assertEquals(-1, notFoundIndex);
    }

    @Test
    void testSearchByPageCount() {
        // Сортируем по количеству страниц
        books.sort(Book.BY_PAGE_COUNT);
        
        Book existingBook = new Book.Builder()
                .name("Any")
                .pageCount(301)
                .publicationYear(2025)
                .build(); 

        int index = BinarySearch.searchBy(books, existingBook, Book.BY_PAGE_COUNT);
        assertEquals(27, index);

        Book nonExistingBook = new Book.Builder()
                .name("Any")
                .pageCount(300)
                .publicationYear(2025)
                .build();

        int notFoundIndex = BinarySearch.searchBy(books, nonExistingBook, Book.BY_PAGE_COUNT);
        assertEquals(-1, notFoundIndex);
    }

    @Test
    void testSearchByPublicationYear() {
        // Сортируем по году публикации
        books.sort(Book.BY_PUBLICATION_YEAR);
        
        Book existingBook = new Book.Builder()
                .name("Any")
                .pageCount(100)
                .publicationYear(2020)
                .build();

        int index = BinarySearch.searchBy(books, existingBook, Book.BY_PUBLICATION_YEAR);
        assertEquals(50, index);

        Book nonExistingBook = new Book.Builder()
                .name("Any")
                .pageCount(100)
                .publicationYear(2025)
                .build();
                
        int notFoundIndex = BinarySearch.searchBy(books, nonExistingBook, Book.BY_PUBLICATION_YEAR);
        assertEquals(-1, notFoundIndex);
    }

    @Test
    void testSearchWithDuplicateValues() {
        // Тест с дублирующимися значениями
        books.add(new Book.Builder()
                .name("Crimson Skies")
                .pageCount(390)
                .publicationYear(2005)
                .build()
                );
        
        books.sort(Book.BY_NAME);
        Book searchBook = new Book.Builder()
                .name("Crimson Skies")
                .pageCount(390)
                .publicationYear(2005)
                .build();
                
        int index = BinarySearch.searchBy(books, searchBook, Book.BY_NAME);
        
        assertEquals("Crimson Skies", books.get(index).getName());
    }

    @Test
    void testSearchEmptyList() {
        List<Book> emptyList = new ArrayList<>();
        Book book = new Book.Builder()
                .name("Test")
                .pageCount(100)
                .publicationYear(2020)
                .build();
        
        int index = BinarySearch.searchBy(emptyList, book, Book.BY_NAME);
        assertEquals(-1, index);
    }

    @Test
    void testSearchSingleElement() {
        List<Book> singleList = List.of(
                new Book.Builder()
                .name("Single")
                .pageCount(100)
                .publicationYear(2020)
                .build()
                );
        
        Book existingBook = new Book.Builder()
                .name("Single")
                .pageCount(100)
                .publicationYear(2020)
                .build();

        int index = BinarySearch.searchBy(singleList, existingBook, Book.BY_NAME);
        assertEquals(0, index);

        Book nonExistingBook = new Book.Builder()
                .name("Other")
                .pageCount(200)
                .publicationYear(2021)
                .build();
                
        int notFoundIndex = BinarySearch.searchBy(singleList, nonExistingBook, Book.BY_NAME);
        assertEquals(-1, notFoundIndex);
    }
}