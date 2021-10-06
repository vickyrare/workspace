package io.codecrafts;

final public class Student {

    private String firstName, lastName;

    public Student(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public static class Builder {

        private String firstName, lastName;

        public Builder() {
            this.firstName = "";
            this.lastName = "";
        }

        public Builder(Student student) {
            this.firstName = student.firstName;
            this.lastName = student.lastName;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
