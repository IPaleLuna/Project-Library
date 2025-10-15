package org.example.sorting;

import org.example.model.Book;
import org.example.model.Reader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class TestEvenFieldSortStrategy {

    // ===== Вспомогательные методы =====

    private Book createBook(String name, int pageCount, int publicationYear) {
        return new Book.Builder()
                .name(name)
                .pageCount(pageCount)
                .publicationYear(publicationYear)
                .build();
    }

    private Reader createReader(String name, int age, String libraryCardNumber) {
        return new Reader.Builder()
                .name(name)
                .age(age)
                .libraryCardNumber(libraryCardNumber)
                .build();
    }

    // ===== Тесты для разных полей Book =====

    @Test
    public void testEvenFieldSortingWithPageCount() {
        List<Book> books = Arrays.asList(
                createBook("Book1", 400, 2020),
                createBook("Book2", 201, 2019),
                createBook("Book3", 100, 2018),
                createBook("Book4", 155, 2021)
        );

        testEvenFieldSortingForBookField(books, "pageCount", Book.BY_PAGE_COUNT);
    }

    @Test
    public void testEvenFieldSortingWithPublicationYear() {
        List<Book> books = Arrays.asList(
                createBook("Book1", 200, 2020), // чётный год
                createBook("Book2", 250, 2019), // нечётный год
                createBook("Book3", 300, 2018), // чётный год
                createBook("Book4", 350, 2021)  // нечётный год
        );

        testEvenFieldSortingForBookField(books, "publicationYear", Book.BY_PUBLICATION_YEAR);
    }

    private void testEvenFieldSortingForBookField(List<Book> books, String fieldName, Comparator<Book> comparator) {
        List<Book> testBooks = new ArrayList<>(books);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenFieldSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenFieldSorter.sort(testBooks, comparator);

        List<Book> evenElements = extractEvenElements(testBooks, fieldName);
        assertTrue(isSorted(evenElements, comparator),
                "Even elements should be sorted for field: " + fieldName);

        executor.shutdown();
    }

    // ===== Тесты для Reader =====

    @Test
    public void testEvenFieldSortingWithReaderAge() {
        List<Reader> readers = Arrays.asList(
                createReader("Alice", 40, "LC001"),
                createReader("Bob", 25, "LC002"),
                createReader("Carol", 20, "LC003"),
                createReader("David", 35, "LC004")
        );

        List<Reader> testReaders = new ArrayList<>(readers);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Sort<Reader> baseSorter = new ParallelQuickSort<Reader>();
        Sort<Reader> evenFieldSorter = new EvenFieldSortStrategy<>("age", baseSorter, executor);

        evenFieldSorter.sort(testReaders, Reader.BY_AGE);

        List<Reader> evenElements = extractEvenElements(testReaders, "age");
        assertTrue(isSorted(evenElements, Reader.BY_AGE),
                "Even elements should be sorted for field: age");

        executor.shutdown();
    }

    // ===== Конкретные тестовые случаи =====

    @Test
    public void testGlobalSortingOfEvenElementsPageCount() {
        List<Book> books = Arrays.asList(
                createBook("Book1", 400, 2020),
                createBook("Book2", 201, 2019),
                createBook("Book3", 100, 2018),
                createBook("Book4", 155, 2021),
                createBook("Book5", 300, 2017),
                createBook("Book6", 233, 2022),
                createBook("Book7", 200, 2023),
                createBook("Book8", 177, 2024)
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenPageCountSorter = new EvenFieldSortStrategy<>("pageCount", baseSorter, executor);

        evenPageCountSorter.sort(books, Book.BY_PAGE_COUNT);

        List<Book> evenPageBooks = extractEvenElements(books, "pageCount");

        assertEquals(4, evenPageBooks.size());
        assertEquals(100, evenPageBooks.get(0).getPageCount());
        assertEquals(200, evenPageBooks.get(1).getPageCount());
        assertEquals(300, evenPageBooks.get(2).getPageCount());
        assertEquals(400, evenPageBooks.get(3).getPageCount());

        Book book2 = findBookByName(books, "Book2");
        Book book4 = findBookByName(books, "Book4");
        Book book6 = findBookByName(books, "Book6");
        Book book8 = findBookByName(books, "Book8");

        assertPreservedRelativeOrder(books, Arrays.asList(book2, book4, book6, book8));

        executor.shutdown();
    }

    @Test
    public void testGlobalSortingWithReadersAge() {
        List<Reader> readers = Arrays.asList(
                createReader("Alice", 40, "LC001"),
                createReader("Bob", 25, "LC002"),
                createReader("Carol", 20, "LC003"),
                createReader("David", 35, "LC004"),
                createReader("Eve", 30, "LC005"),
                createReader("Frank", 22, "LC006")
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Reader> baseSorter = new ParallelQuickSort<Reader>();
        Sort<Reader> evenAgeSorter = new EvenFieldSortStrategy<>("age", baseSorter, executor);

        evenAgeSorter.sort(readers, Reader.BY_AGE);

        List<Reader> evenAgeReaders = extractEvenElements(readers, "age");

        assertEquals(4, evenAgeReaders.size());
        assertEquals(20, evenAgeReaders.get(0).getAge());
        assertEquals(22, evenAgeReaders.get(1).getAge());
        assertEquals(30, evenAgeReaders.get(2).getAge());
        assertEquals(40, evenAgeReaders.get(3).getAge());

        executor.shutdown();
    }

    @Test
    public void testEvenFieldSortingPreservesOddElementsPositionsForPageCount() {
        List<Book> books = Arrays.asList(
                createBook("A", 201, 2020),
                createBook("B", 400, 2020),
                createBook("C", 203, 2020),
                createBook("D", 100, 2020),
                createBook("E", 205, 2020),
                createBook("F", 300, 2020)
        );

        testEvenFieldSortingPreservesOddElements(books, "pageCount", Book.BY_PAGE_COUNT);
    }

    @Test
    public void testEvenFieldSortingPreservesOddElementsPositionsForPublicationYear() {
        List<Book> books = Arrays.asList(
                createBook("A", 200, 2021),
                createBook("B", 250, 2020),
                createBook("C", 300, 2023),
                createBook("D", 350, 2018),
                createBook("E", 400, 2025),
                createBook("F", 450, 2022)
        );

        testEvenFieldSortingPreservesOddElements(books, "publicationYear", Book.BY_PUBLICATION_YEAR);
    }

    private void testEvenFieldSortingPreservesOddElements(List<Book> books, String fieldName, Comparator<Book> comparator) {
        List<Book> testBooks = new ArrayList<>(books);
        List<Book> originalOddBooks = extractOddElements(testBooks, fieldName);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenSorter.sort(testBooks, comparator);

        List<Book> resultingOddBooks = extractOddElements(testBooks, fieldName);
        assertEquals(originalOddBooks, resultingOddBooks,
                "Odd elements should preserve their relative order for field: " + fieldName);

        executor.shutdown();
    }

    // ===== Тесты корректности граничных случаев =====

    @Test
    public void testWithAllElementsEvenPageCount() {
        List<Book> allEvenBooks = Arrays.asList(
                createBook("Z", 400, 2020),
                createBook("A", 100, 2018),
                createBook("M", 200, 2022),
                createBook("B", 300, 2024)
        );

        testWithAllElementsEven(allEvenBooks, "pageCount", Book.BY_PAGE_COUNT);
    }

    @Test
    public void testWithAllElementsEvenPublicationYear() {
        List<Book> allEvenBooks = Arrays.asList(
                createBook("Z", 200, 2020),
                createBook("A", 250, 2018),
                createBook("M", 300, 2022),
                createBook("B", 350, 2024)
        );

        testWithAllElementsEven(allEvenBooks, "publicationYear", Book.BY_PUBLICATION_YEAR);
    }

    private void testWithAllElementsEven(List<Book> books, String fieldName, Comparator<Book> comparator) {
        List<Book> testBooks = new ArrayList<>(books);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenSorter.sort(testBooks, comparator);

        assertTrue(isSorted(testBooks, comparator),
                "All even collection should be fully sorted for field: " + fieldName);

        executor.shutdown();
    }

    @Test
    public void testWithSingleEvenElementPageCount() {
        List<Book> books = Arrays.asList(
                createBook("A", 201, 2021),
                createBook("B", 200, 2020),
                createBook("C", 203, 2023)
        );

        testWithSingleEvenElement(books, "pageCount", Book.BY_PAGE_COUNT, 200);
    }

    @Test
    public void testWithSingleEvenElementPublicationYear() {
        List<Book> books = Arrays.asList(
                createBook("A", 200, 2021),
                createBook("B", 250, 2020),
                createBook("C", 300, 2023)
        );

        testWithSingleEvenElement(books, "publicationYear", Book.BY_PUBLICATION_YEAR, 2020);
    }

    private void testWithSingleEvenElement(List<Book> books, String fieldName, Comparator<Book> comparator, int expectedEvenValue) {
        List<Book> testBooks = new ArrayList<>(books);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenSorter.sort(testBooks, comparator);

        Book evenBook = findBookByFieldValue(testBooks, fieldName, expectedEvenValue);
        assertNotNull(evenBook, "Even element should be found for field: " + fieldName);

        executor.shutdown();
    }

    @Test
    public void testWithNoEvenElementsPageCount() {
        List<Book> allOddBooks = Arrays.asList(
                createBook("A", 201, 2021),
                createBook("B", 203, 2023),
                createBook("C", 205, 2025)
        );

        testWithNoEvenElements(allOddBooks, "pageCount", Book.BY_PAGE_COUNT);
    }

    @Test
    public void testWithNoEvenElementsPublicationYear() {
        List<Book> allOddBooks = Arrays.asList(
                createBook("A", 200, 2021),
                createBook("B", 250, 2023),
                createBook("C", 300, 2025)
        );

        testWithNoEvenElements(allOddBooks, "publicationYear", Book.BY_PUBLICATION_YEAR);
    }

    private void testWithNoEvenElements(List<Book> books, String fieldName, Comparator<Book> comparator) {
        List<Book> testBooks = new ArrayList<>(books);
        List<Book> originalOrder = new ArrayList<>(testBooks);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenSorter.sort(testBooks, comparator);

        assertEquals(originalOrder, testBooks,
                "Collection with no even elements should remain unchanged for field: " + fieldName);

        executor.shutdown();
    }

    // ===== Вспомогательные методы для проверок =====

    private <T> List<T> extractEvenElements(List<T> list, String fieldName) {
        List<T> evenElements = new ArrayList<>();
        for (T element : list) {
            if (isEvenField(element, fieldName)) {
                evenElements.add(element);
            }
        }
        return evenElements;
    }

    private <T> List<T> extractOddElements(List<T> list, String fieldName) {
        List<T> oddElements = new ArrayList<>();
        for (T element : list) {
            if (!isEvenField(element, fieldName)) {
                oddElements.add(element);
            }
        }
        return oddElements;
    }

    private <T> boolean isEvenField(T element, String fieldName) {
        try {
            java.lang.reflect.Field field = element.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(element);

            if (value instanceof Integer) {
                return ((Integer) value) % 2 == 0;
            } else if (value instanceof Long) {
                return ((Long) value) % 2 == 0;
            } else if (value instanceof Double) {
                double doubleValue = (Double) value;
                return ((long) doubleValue) % 2 == 0;
            } else if (value instanceof Float) {
                float floatValue = (Float) value;
                return ((long) floatValue) % 2 == 0;
            } else if (value instanceof Short) {
                short shortValue = (Short) value;
                return shortValue % 2 == 0;
            } else if (value instanceof Byte) {
                byte byteValue = (Byte) value;
                return byteValue % 2 == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking field: " + fieldName, e);
        }
        return false;
    }

    private Book findBookByName(List<Book> books, String name) {
        return books.stream()
                .filter(book -> book.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Book not found: " + name));
    }

    private Book findBookByFieldValue(List<Book> books, String fieldName, int expectedValue) {
        return books.stream()
                .filter(book -> {
                    try {
                        java.lang.reflect.Field field = book.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(book);
                        return value instanceof Integer && (Integer) value == expectedValue;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    private <T> void assertPreservedRelativeOrder(List<T> list, List<T> elements) {
        List<Integer> indices = new ArrayList<>();
        for (T element : elements) {
            indices.add(list.indexOf(element));
        }

        for (int i = 0; i < indices.size() - 1; i++) {
            assertTrue(indices.get(i) < indices.get(i + 1),
                    "Elements should preserve their relative order");
        }
    }

    private <T> boolean isSorted(List<T> list, Comparator<T> comparator) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    // ===== Тесты производительности с большой коллекцией =====

    @Test
    public void testLargeCollectionGlobalSortingPageCount() {
        testLargeCollectionGlobalSorting("pageCount", Book.BY_PAGE_COUNT);
    }

    @Test
    public void testLargeCollectionGlobalSortingPublicationYear() {
        testLargeCollectionGlobalSorting("publicationYear", Book.BY_PUBLICATION_YEAR);
    }

    private void testLargeCollectionGlobalSorting(String fieldName, Comparator<Book> comparator) {
        List<Book> largeCollection = new ArrayList<>();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            largeCollection.add(createBook(
                    "Book" + i,
                    random.nextInt(1000) + 1,
                    1900 + random.nextInt(124)
            ));
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> evenSorter = new EvenFieldSortStrategy<>(fieldName, baseSorter, executor);

        evenSorter.sort(largeCollection, comparator);

        List<Book> evenBooks = extractEvenElements(largeCollection, fieldName);
        assertTrue(isSorted(evenBooks, comparator),
                "All even elements should be globally sorted in large collection for field: " + fieldName);

        executor.shutdown();
    }

    // ===== Тесты обработки ошибок =====

    @Test
    public void testInvalidFieldThrowsException() {
        List<Book> books = Arrays.asList(
                createBook("Book1", 200, 2020),
                createBook("Book2", 300, 2021)
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Sort<Book> baseSorter = new ParallelMergeSort<Book>();
        Sort<Book> invalidFieldSorter = new EvenFieldSortStrategy<>("nonExistentField", baseSorter, executor);

        assertThrows(RuntimeException.class, () -> invalidFieldSorter.sort(books),
                "Should throw exception for non-existent field");

        executor.shutdown();
    }

    @Test
    public void testDifferentBaseSortingStrategies() {
        List<Book> books = Arrays.asList(
                createBook("Z", 400, 2020),
                createBook("A", 100, 2018),
                createBook("M", 200, 2022),
                createBook("B", 300, 2024)
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Sort<Book>[] baseSorters = new Sort[]{
                new ParallelMergeSort<Book>(),
                new ParallelQuickSort<Book>(),
                new ParallelTimSort<Book>()
        };

        for (Sort<Book> baseSorter : baseSorters) {
            List<Book> testBooks = new ArrayList<>(books);
            Sort<Book> evenSorter = new EvenFieldSortStrategy<>("pageCount", baseSorter, executor);

            evenSorter.sort(testBooks, Book.BY_PAGE_COUNT);

            List<Book> evenBooks = extractEvenElements(testBooks, "pageCount");
            assertTrue(isSorted(evenBooks, Book.BY_PAGE_COUNT),
                    "Even elements should be sorted with base sorter: " + baseSorter.getClass().getSimpleName());
        }

        executor.shutdown();
    }
}