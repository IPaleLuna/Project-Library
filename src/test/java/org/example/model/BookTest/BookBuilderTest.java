package org.example.model.BookTest;

import org.example.model.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookBuilderTest {

    @Test
    void builderCreatesCorrectObject() {
        Book book = new Book.Builder()
                .name("Effective Java")
                .pageCount(412)
                .publicationYear(2018)
                .build();

        assertEquals("Effective Java", book.getName());
        assertEquals(412, book.getPageCount());
        assertEquals(2018, book.getPublicationYear());
    }

    @Test
    void builderOrderIndependence() {
        Book book1 = new Book.Builder()
                .name("Test")
                .pageCount(100)
                .publicationYear(2020)
                .build();

        Book book2 = new Book.Builder()
                .publicationYear(2020)
                .name("Test")
                .pageCount(100)
                .build();

        assertEquals(book1, book2);
    }
}