package org.example.model.BookTest;

import org.example.model.Book;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class BookComparatorTest {

    @Test
    void testByNameComparator() {
        Book b1 = new Book.Builder().name("A").pageCount(100).publicationYear(2000).build();
        Book b2 = new Book.Builder().name("B").pageCount(100).publicationYear(2000).build();

        assertTrue(Book.BY_NAME.compare(b1, b2) < 0);
    }

    @Test
    void testByPageCountComparator() {
        Book b1 = new Book.Builder().name("X").pageCount(100).publicationYear(2000).build();
        Book b2 = new Book.Builder().name("X").pageCount(200).publicationYear(2000).build();

        assertTrue(Book.BY_PAGE_COUNT.compare(b1, b2) < 0);
    }

    @Test
    void testNaturalOrderConsistency() {
        Book b1 = new Book.Builder().name("A").pageCount(100).publicationYear(2000).build();
        Book b2 = new Book.Builder().name("B").pageCount(100).publicationYear(2000).build();

        int compareToResult = b1.compareTo(b2);
        int naturalOrderResult = Comparator.<Book>naturalOrder().compare(b1, b2);
        assertEquals(compareToResult, naturalOrderResult);
    }
}