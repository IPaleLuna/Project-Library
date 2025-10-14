package org.example.model.BookTest;

import org.example.model.Book;

public class BookValidationTest {

    public static void main(String[] args) {
        System.out.println("=== Running Book Validation Tests ===");

        testValidBook();
        testEmptyName();
        testNullName();
        testNegativePageCount();
        testZeroPageCount();
        testPublicationYearBefore1600();
        testPublicationYearInFuture();

        System.out.println("\nAll Book validation tests completed.");
    }

    private static void testValidBook() {
        try {
            Book book = new Book.Builder()
                    .name("Clean Code")
                    .pageCount(464)
                    .publicationYear(2008)
                    .build();
            System.out.println("Valid book created successfully.");
        } catch (Exception e) {
            System.out.println("Valid book failed: " + e.getMessage());
        }
    }

    private static void testEmptyName() {
        try {
            new Book.Builder().name("").pageCount(100).publicationYear(2000).build();
            System.out.println("Empty name should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Empty name correctly rejected: " + e.getMessage());
        }
    }

    private static void testNullName() {
        try {
            new Book.Builder().name(null).pageCount(100).publicationYear(2000).build();
            System.out.println("Null name should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Null name correctly rejected: " + e.getMessage());
        }
    }

    private static void testNegativePageCount() {
        try {
            new Book.Builder().name("Test").pageCount(-1).publicationYear(2000).build();
            System.out.println("Negative page count should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Negative page count correctly rejected: " + e.getMessage());
        }
    }

    private static void testZeroPageCount() {
        try {
            new Book.Builder().name("Test").pageCount(0).publicationYear(2000).build();
            System.out.println("Zero page count should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Zero page count correctly rejected: " + e.getMessage());
        }
    }

    private static void testPublicationYearBefore1600() {
        try {
            new Book.Builder().name("Test").pageCount(100).publicationYear(1599).build();
            System.out.println("Year 1599 should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Year before 1600 correctly rejected: " + e.getMessage());
        }
    }

    private static void testPublicationYearInFuture() {
        try {
            new Book.Builder().name("Test").pageCount(100).publicationYear(3000).build();
            System.out.println("Year 3000 should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Year too far in future correctly rejected: " + e.getMessage());
        }
    }
}