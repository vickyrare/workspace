package io.codecrafts;

public class Main {

    public static void main(String[] args) {
	    Student student = new Student.Builder().setFirstName("John").setLastName("Doe").build();
        System.out.println(student);
    }
}
