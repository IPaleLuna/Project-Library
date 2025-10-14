package org.example.app;

import org.example.model.Book;
import org.example.sorting.ParallelMergeSort;
import org.example.sorting.ParallelQuickSort;

import org.example.sorting.ParallelTimSort;
import org.example.sorting.Sort;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Data Processor ===");
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("Working with Books...");
                    //заглушка, сюда потом сортировку/ввод вывод и всё остальное
                    break;
                }
                case "2" -> {
                    System.out.println("Working with Reader...");
                    //тож заглушка
                    break;
                }
                case "0" -> {
                    System.out.println("Terminated");
        }

        System.out.println();

        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelMergeSort mergeSort = new ParallelMergeSort();
        mergeSort.sort(list2);

        for (int i = 0; i < list2.size(); i++) {
            System.out.printf(list2.get(i) + " ");
        }

        System.out.println();

        List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 5, -11, 3, 20, 6, 10, 28));

        ParallelTimSort timSort = new ParallelTimSort();
        timSort.sort(list3);

        for (int i = 0; i < list3.size(); i++) {
            System.out.printf(list3.get(i) + " ");
        }

        System.out.println();

        File file = new File("src/main/resources/books.json");
        if (!file.exists()) {
            System.out.println(file.getAbsolutePath());

            return;
        }
        List<Book> books = JsonLoader.loadList(file, new TypeReference<>() {});


        books.forEach(System.out::println);

    }
}
