package io.codecrafts;

import java.util.List;

public class FlatMap {
    public static void main(String[] args) {
        List<List<String>> names = List.of(
                List.of("Alice", "Bob"),
                List.of("Charlie", "David")
        );

        names.stream()
                .map(list -> list.stream())
                .forEach(System.out::println);

        names.stream()
                .flatMap(list -> list.stream())
                .forEach(System.out::println);

    }
}
