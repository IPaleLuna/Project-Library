package org.example.collections;

import org.example.model.Book;
import org.example.model.Reader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomCollection {

    // ===== Тестовые данные =====

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

    private List<Book> createSampleBooks() {
        return Arrays.asList(
                createBook("The Great Gatsby", 218, 1925),
                createBook("1984", 328, 1949),
                createBook("To Kill a Mockingbird", 281, 1960),
                createBook("The Hobbit", 310, 1937),
                createBook("Pride and Prejudice", 432, 1813)
        );
    }

    private List<Reader> createSampleReaders() {
        return Arrays.asList(
                createReader("Alice Johnson", 25, "LC001"),
                createReader("Bob Smith", 30, "LC002"),
                createReader("Carol Davis", 22, "LC003"),
                createReader("David Wilson", 35, "LC004"),
                createReader("Eve Brown", 28, "LC005")
        );
    }

    // ===== Тесты базовой функциональности List =====

    @Test
    public void testEmptyCollection() {
        CustomCollection<Book> collection = new CustomCollection<>();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    @Test
    public void testAddAndGet() {
        CustomCollection<Book> collection = new CustomCollection<>();
        Book book = createBook("Test Book", 100, 2020);

        collection.add(book);
        assertFalse(collection.isEmpty());
        assertEquals(1, collection.size());
        assertEquals(book, collection.getFirst());
    }

    @Test
    public void testAddWithIndex() {
        CustomCollection<Book> collection = new CustomCollection<>();
        Book book1 = createBook("First", 100, 2020);
        Book book2 = createBook("Second", 200, 2021);

        collection.add(book1);
        collection.add(0, book2); // Добавляем в начало

        assertEquals(2, collection.size());
        assertEquals(book2, collection.get(0));
        assertEquals(book1, collection.get(1));
    }

    @Test
    public void testGetInvalidIndex() {
        CustomCollection<Book> collection = new CustomCollection<>();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            collection.get(0); // Должен бросить исключение
        });
    }

    @Test
    public void testAddNullElement() {
        CustomCollection<Book> collection = new CustomCollection<>();
        assertThrows(NullPointerException.class, () -> {
            collection.add(null);
        });
    }

    @Test
    public void testContains() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        Book sampleBook = createSampleBooks().getFirst();

        assertTrue(collection.contains(sampleBook));
        assertFalse(collection.contains(createBook("Non-existent", 100, 2020)));
    }

    @Test
    public void testRemoveByIndex() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        Book firstBook = collection.get(0);
        Book removed = collection.remove(0);

        assertEquals(firstBook, removed);
        assertEquals(4, collection.size());
        assertFalse(collection.contains(firstBook));
    }

    @Test
    public void testRemoveByObject() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        Book bookToRemove = createSampleBooks().get(2);

        assertTrue(collection.remove(bookToRemove));
        assertEquals(4, collection.size());
        assertFalse(collection.contains(bookToRemove));
    }

    @Test
    public void testSet() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        Book newBook = createBook("New Book", 150, 2023);
        Book oldBook = collection.set(0, newBook);

        assertEquals(newBook, collection.get(0));
        assertNotEquals(oldBook, collection.get(0));
    }

    // ===== Тесты кастомных методов =====

    @Test
    public void testFilter() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());

        // Фильтруем книги с количеством страниц больше 300
        CustomCollection<Book> filtered = collection.filter(book -> book.getPageCount() > 300);

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(book -> book.getPageCount() > 300));
    }

    @Test
    public void testMap() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());

        // Преобразуем книги в их названия
        CustomCollection<String> bookNames = collection.map(Book::getName);

        assertEquals(collection.size(), bookNames.size());
        assertTrue(bookNames.contains("The Great Gatsby"));
        assertTrue(bookNames.contains("1984"));
    }

    @Test
    public void testForEachWithIndex() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> collectedBooks = new ArrayList<>();
        List<Integer> collectedIndices = new ArrayList<>();

        collection.forEachWithIndex((book, index) -> {
            collectedBooks.add(book);
            collectedIndices.add(index);
        });

        assertEquals(collection.size(), collectedBooks.size());
        assertEquals(collection.size(), collectedIndices.size());

        for (int i = 0; i < collectedIndices.size(); i++) {
            assertEquals(Integer.valueOf(i), collectedIndices.get(i));
            assertEquals(collection.get(i), collectedBooks.get(i));
        }
    }

    @Test
    public void testGetAverageForBooks() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());

        OptionalDouble averagePages = collection.getAverage("pageCount");
        OptionalDouble averageYear = collection.getAverage("publicationYear");

        assertTrue(averagePages.isPresent());
        assertTrue(averageYear.isPresent());

        double expectedPageAvg = createSampleBooks().stream()
                .mapToInt(Book::getPageCount)
                .average()
                .orElseThrow(() -> new IllegalStateException("Expected non-empty stream"));

        assertEquals(expectedPageAvg, averagePages.getAsDouble(), 0.001);
    }

    @Test
    public void testGetAverageForReaders() {
        CustomCollection<Reader> collection = new CustomCollection<>(createSampleReaders());

        OptionalDouble averageAge = collection.getAverage("age");

        assertTrue(averageAge.isPresent());

        double expectedAgeAvg = createSampleReaders().stream()
                .mapToInt(Reader::getAge)
                .average()
                .orElseThrow(() -> new IllegalStateException("Expected non-empty stream"));

        assertEquals(expectedAgeAvg, averageAge.getAsDouble(), 0.001);
    }

    @Test
    public void testGetAverageWithNullElements() {
        CustomCollection<Book> collection = new CustomCollection<>();
        collection.add(createBook("Book1", 100, 2020));
        collection.add(null); // null элемент должен быть отфильтрован
        collection.add(createBook("Book2", 200, 2021));

        OptionalDouble average = collection.getAverage("pageCount");

        assertTrue(average.isPresent());
        assertEquals(150.0, average.getAsDouble(), 0.001);
    }

    @Test
    public void testGetAverageInvalidField() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        assertThrows(RuntimeException.class, () -> {
            collection.getAverage("nonExistentField"); // Должен бросить исключение
        });
    }

    // ===== Тесты производительности containsAll =====

    @Test
    public void testContainsAllSmallCollection() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> subCollection = createSampleBooks().subList(1, 3);

        assertTrue(collection.containsAll(subCollection));
    }

    @Test
    public void testContainsAllLargeCollection() {
        // Создаем большую коллекцию для тестирования оптимизации
        CustomCollection<Integer> largeCollection = new CustomCollection<>();
        for (int i = 0; i < 20; i++) {
            largeCollection.add(i);
        }

        List<Integer> checkList = Arrays.asList(5, 10, 15);
        assertTrue(largeCollection.containsAll(checkList));
    }

    @Test
    public void testNotContainsAll() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> mixedCollection = new ArrayList<>(createSampleBooks().subList(1, 3));
        mixedCollection.add(createBook("Extra Book", 100, 2020));

        assertFalse(collection.containsAll(mixedCollection));
    }

    // ===== Тесты граничных случаев =====

    @Test
    public void testSubList() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> subList = collection.subList(1, 3);

        assertEquals(2, subList.size());
        assertEquals(createSampleBooks().get(1), subList.get(0));
        assertEquals(createSampleBooks().get(2), subList.get(1));
    }

    @Test
    public void testSubListInvalidRange() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        assertThrows(IllegalArgumentException.class, () -> {
            collection.subList(3, 1); // fromIndex > toIndex
        });
    }

    @Test
    public void testIterator() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> iteratedBooks = new ArrayList<>();

        for (Book book : collection) {
            iteratedBooks.add(book);
        }

        assertEquals(collection.size(), iteratedBooks.size());
        assertTrue(iteratedBooks.containsAll(collection));
    }

    @Test
    public void testListIterator() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        ListIterator<Book> iterator = collection.listIterator();

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(collection.size(), count);
    }

    @Test
    public void testEqualsAndHashCode() {
        CustomCollection<Book> collection1 = new CustomCollection<>(createSampleBooks());
        CustomCollection<Book> collection2 = new CustomCollection<>(createSampleBooks());
        CustomCollection<Book> differentCollection = new CustomCollection<>();

        assertEquals(collection1, collection2);
        assertEquals(collection1.hashCode(), collection2.hashCode());
        assertNotEquals(collection1, differentCollection);
    }

    @Test
    public void testToString() {
        CustomCollection<Book> collection = new CustomCollection<>();
        collection.add(createBook("Test", 100, 2020));

        String stringRepresentation = collection.toString();
        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains("Test"));
    }

    // ===== Тесты с Reader классом =====

    @Test
    public void testCustomCollectionWithReaders() {
        CustomCollection<Reader> readers = new CustomCollection<>(createSampleReaders());

        assertEquals(5, readers.size());
        assertTrue(readers.contains(createSampleReaders().get(0)));

        // Тестируем фильтрацию по возрасту
        CustomCollection<Reader> youngReaders = readers.filter(reader -> reader.getAge() < 30);
        assertTrue(youngReaders.stream().allMatch(reader -> reader.getAge() < 30));

        // Тестируем преобразование
        CustomCollection<String> cardNumbers = readers.map(Reader::getLibraryCardNumber);
        assertTrue(cardNumbers.contains("LC001"));
    }

    @Test
    public void testMixedOperations() {
        CustomCollection<Book> books = new CustomCollection<>(createSampleBooks());

        // Комбинируем несколько операций
        CustomCollection<String> longBookNames = books
                .filter(book -> book.getPageCount() > 300)
                .map(Book::getName)
                .filter(name -> name.length() > 10);

        assertTrue(longBookNames.size() <= 2); // Pride and Prejudice и возможно другие
    }

    // ===== Тесты потокобезопасности (базовые) =====

    @Test
    public void testConcurrentModificationInForEach() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());

        // Это должно работать без ConcurrentModificationException
        for (Book book : collection) {
            assertNotNull(book);
        }
    }

    // ===== Тесты с edge cases =====

    @Test
    public void testEmptyFilter() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        CustomCollection<Book> emptyFiltered = collection.filter(book -> false);

        assertTrue(emptyFiltered.isEmpty());
    }

    @Test
    public void testClear() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        assertFalse(collection.isEmpty());

        collection.clear();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
    }

    @Test
    public void testRetainAll() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> booksToRetain = createSampleBooks().subList(0, 2);

        assertTrue(collection.retainAll(booksToRetain));
        assertEquals(2, collection.size());
        assertTrue(collection.containsAll(booksToRetain));
    }

    @Test
    public void testRemoveAll() {
        CustomCollection<Book> collection = new CustomCollection<>(createSampleBooks());
        List<Book> booksToRemove = createSampleBooks().subList(0, 2);

        assertTrue(collection.removeAll(booksToRemove));
        assertEquals(3, collection.size());
        assertFalse(collection.contains(booksToRemove.get(0)));
    }
}