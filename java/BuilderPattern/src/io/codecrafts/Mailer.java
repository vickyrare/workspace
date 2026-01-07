package io.codecrafts;

import java.util.function.Consumer;

// Fluent Interface
public class Mailer {
    public Mailer from(String addr) {
        System.out.println("from");
        return this;
    }

    public Mailer to(String addr) {
        System.out.println("to");
        return this;
    }

    public Mailer subject(String line) {
        System.out.println("subject");
        return this;
    }

    public Mailer body(String message) {
        System.out.println("body");
        return this;
    }

    public static void send(Consumer<Mailer> block) {
        Mailer mailer = new Mailer();
        block.accept(mailer);
        System.out.println("Sending...");
    }

    public static void main(String[] args) {
        Mailer.send(mailer ->
                mailer.from("from")
                        .to("to")
                        .subject("subject")
                        .body("body"));
//        int total = 0;
//        orders.stream()
//                .filter(order -> orderIsActive())
//                .foreach(order -> {
//                    total += order.getPrice()
//                });

//        int total = orders.stream()
//                .filter(Order::isActive)
//                .mapToInt(Order::getPrice)
//                .sum();
    }
}
