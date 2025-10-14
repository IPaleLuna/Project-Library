package org.example.model;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonPOJOBuilder;
package model;

import java.util.Comparator;
import java.util.Objects;

@JsonDeserialize(builder = Reader.Builder.class)
public class Reader implements Comparable<Reader> {
    public static final Comparator<Reader> BY_AGE = Comparator.comparingInt(Reader::getAge);
    public static final Comparator<Reader> BY_NAME = Comparator.comparing(Reader::getName);
    public static final Comparator<Reader> BY_LIBRARY_CARD = Comparator.comparing(Reader::getLibraryCardNumber);

    private final int age;
    private final String name;
    private final String libraryCardNumber;

    private Reader(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.libraryCardNumber = builder.libraryCardNumber;
        validate();
    }

    private void validate() {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (libraryCardNumber == null || libraryCardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Library card number cannot be empty");
        }
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getLibraryCardNumber() {
        return libraryCardNumber;
    }

    @Override
    public int compareTo(Reader other) {
        int ageCompare = Integer.compare(this.age, other.age);
        if (ageCompare != 0) return ageCompare;

        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) return nameCompare;

        return this.libraryCardNumber.compareTo(other.libraryCardNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reader reader = (Reader) obj;
        return age == reader.age &&
                Objects.equals(name, reader.name) &&
                Objects.equals(libraryCardNumber, reader.libraryCardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, libraryCardNumber);
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "', age=" + age + ", libraryCardNumber='" + libraryCardNumber + "'}";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private int age;
        private String name;
        private String libraryCardNumber;

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder libraryCardNumber(String libraryCardNumber) {
            this.libraryCardNumber = libraryCardNumber;
            return this;
        }

        public Reader build() {
            return new Reader(this);
        }
    }
}