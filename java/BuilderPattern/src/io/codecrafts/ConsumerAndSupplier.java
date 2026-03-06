package io.codecrafts;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConsumerAndSupplier {
    public static void main(String[] args) {
        Consumer<String> printConsumer = System.out::println;
        printConsumer.accept("Hello");

        Supplier<Double> randomSupplier = Math::random;
        System.out.println(randomSupplier.get());
    }
}
