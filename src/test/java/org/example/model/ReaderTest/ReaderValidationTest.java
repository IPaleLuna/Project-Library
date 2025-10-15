package org.example.model.ReaderTest;

import org.example.model.Reader;

public class ReaderValidationTest {

    public static void main(String[] args) {
        System.out.println("=== Running Reader Validation Tests ===");

        testValidReader();
        testEmptyName();
        testNullName();
        testNegativeAge();
        testAgeTooHigh();
        testEmptyLibraryCard();
        testNullLibraryCard();

        System.out.println("All Reader validation tests completed.");
    }

    private static void testValidReader() {
        try {
            Reader reader = new Reader.Builder()
                    .name("John Doe")
                    .age(25)
                    .libraryCardNumber("LIB-2025-001")
                    .build();
            System.out.println("Valid reader created successfully.");
        } catch (Exception e) {
            System.out.println("Valid reader failed: " + e.getMessage());
        }
    }

    private static void testEmptyName() {
        try {
            new Reader.Builder().name("").age(20).libraryCardNumber("LIB-1").build();
            System.out.println("Empty name should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Empty name correctly rejected: " + e.getMessage());
        }
    }

    private static void testNullName() {
        try {
            new Reader.Builder().name(null).age(20).libraryCardNumber("LIB-1").build();
            System.out.println("Null name should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Null name correctly rejected: " + e.getMessage());
        }
    }

    private static void testNegativeAge() {
        try {
            new Reader.Builder().name("John").age(-5).libraryCardNumber("LIB-1").build();
            System.out.println("Negative age should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Negative age correctly rejected: " + e.getMessage());
        }
    }

    private static void testAgeTooHigh() {
        try {
            new Reader.Builder().name("John").age(151).libraryCardNumber("LIB-1").build();
            System.out.println("Age > 150 should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Age too high correctly rejected: " + e.getMessage());
        }
    }

    private static void testEmptyLibraryCard() {
        try {
            new Reader.Builder().name("John").age(25).libraryCardNumber("").build();
            System.out.println("Empty library card should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Empty library card correctly rejected: " + e.getMessage());
        }
    }

    private static void testNullLibraryCard() {
        try {
            new Reader.Builder().name("John").age(25).libraryCardNumber(null).build();
            System.out.println("Null library card should have failed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Null library card correctly rejected: " + e.getMessage());
        }
    }
}