package io.codecrafts;

import java.util.function.Supplier;

public class LazinessWithLambda {

    public static int compute(int number) {
        System.out.println("Slow operation");
        return number * 100;
    }
    public static void operateNormalOrder(Supplier<Integer> supplier) {
        if (Math.random() > 0.5) {
            System.out.println("Use the value " + supplier.get());
        } else {
            System.out.println("Continue without using the value");
        }
    }

    public static void main(String[] args) {
        operateNormalOrder(() -> compute(20));
    }
}
