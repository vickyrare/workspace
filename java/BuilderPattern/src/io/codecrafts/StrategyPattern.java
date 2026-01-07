package io.codecrafts;

import java.util.List;
import java.util.function.Predicate;

public class StrategyPattern {
    public static void main(String[] args) {
        List<Integer> values = List.of(1, 2, 3, 4, 5);
        System.out.println(totalValues(values, e -> true));
        System.out.println(totalValues(values, StrategyPattern::isOdd));
        System.out.println(totalValues(values, StrategyPattern::isEven));
    }

    public static boolean isOdd(Integer num) {
        return num % 2 != 0;
    }

    public static boolean isEven(Integer num) {
        return num % 2 == 0;
    }

    public static Integer totalValues(List<Integer> values, Predicate<Integer> selector) {
        return values.stream()
                .filter(selector)
                .mapToInt(e -> e)
                .sum();
    }
}
