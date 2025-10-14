package org.example.datasource.Reader;

import org.example.collections.CustomCollection;
import org.example.model.Reader;
import org.example.util.JsonLoader;
import tools.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReaderFileInput implements ReaderDataSource {

    @Override
    public CustomCollection<Reader> generateReaders() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file path (default: readers.json): ");
        String filePath = scanner.nextLine().trim();
        if (filePath.isEmpty()) {
            filePath = "readers.json";
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return new CustomCollection<>();
        }

        try {
            CustomCollection<Reader> readers = JsonLoader.loadList(file, new TypeReference<CustomCollection<Reader>>() {});
            System.out.println("Loaded " + readers.size() + " readers from '" + filePath + "'");
            return readers;
        } catch (Exception e) {
            System.err.println("Error loading readers: " + e.getMessage());
            return new CustomCollection<>();
        }
    }
}