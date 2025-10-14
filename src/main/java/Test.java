import java.io.File;
import java.util.Arrays;

import org.example.collections.CustomCollection;
import org.example.model.Book;
import org.example.search.BinarySearch;
import org.example.util.JsonLoader;


public class Test {
    public static void main(String[] args) {
        CustomCollection<Book> books = new CustomCollection(Arrays.asList(
            new Book.Builder().name("The Lord of the Rings").pageCount(1178).publicationYear(1954).build(),
            new Book.Builder().name("1984").pageCount(328).publicationYear(1949).build(),
            new Book.Builder().name("To Kill a Mockingbird").pageCount(281).publicationYear(1960).build(),
            new Book.Builder().name("The Great Gatsby").pageCount(218).publicationYear(1925).build(),
            new Book.Builder().name("Pride and Prejudice").pageCount(432).publicationYear(1813).build()
        ));

        
        try {
            JsonLoader.save(new File("users.json"), books);
            System.out.println("Пользователи сохранены");
            
            String json = JsonLoader.toJson(books);
            System.out.println("JSON строка: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
