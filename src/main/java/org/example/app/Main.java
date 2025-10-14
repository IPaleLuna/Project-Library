package org.example.app;

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
                    return;
                }
                default -> System.out.println("Incorrect, only 1,2 or 0");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Process Books");
        System.out.println("2. Process Readers");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }
}
