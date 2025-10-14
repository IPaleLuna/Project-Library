package org.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.Objects;

public class Book implements Comparable<Book> {
    public static final Comparator<Book> BY_PAGE_COUNT = Comparator.comparingInt(Book::getPageCount);
    public static final Comparator<Book> BY_NAME = Comparator.comparing(Book::getName);
    public static final Comparator<Book> BY_PUBLICATION_YEAR = Comparator.comparingInt(Book::getPublicationYear);

    private final int pageCount;
    private final String name;
    private final int publicationYear;

    @JsonCreator
    public Book(
            @JsonProperty("name") String name,
            @JsonProperty("pageCount") int pageCount,
            @JsonProperty("publicationYear") int publicationYear
    ){
        this.name = name;
        this.pageCount = pageCount;
        this.publicationYear = publicationYear;
    }

    private Book(Builder builder) {
        this.name = builder.name;
        this.pageCount = builder.pageCount;
        this.publicationYear = builder.publicationYear;
        validate();
    }

    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (publicationYear < 1600 || publicationYear > java.time.Year.now().getValue() + 1) {
            throw new IllegalArgumentException("Publication year must be between 1600 and next year");
        }
        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be positive");
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getName() {
        return name;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public int compareTo(Book other) {
        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }

        int pageCountCompare = Integer.compare(this.pageCount, other.pageCount);
        if (pageCountCompare != 0) {
            return pageCountCompare;
        }

        return Integer.compare(this.publicationYear, other.publicationYear);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        return pageCount == book.pageCount && publicationYear == book.publicationYear && Objects.equals(name, book.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pageCount, publicationYear);
    }

    @Override
    public String toString() {
        return "Book{pageCount=" + pageCount + ", name=\"" + name + "\", publicationYear=" + publicationYear + "}";
    }


    public static class Builder {
        private int pageCount;
        private String name;
        private int publicationYear;

        public Builder pageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder publicationYear(int publicationYear) {
            this.publicationYear = publicationYear;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}