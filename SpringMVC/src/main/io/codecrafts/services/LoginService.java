package io.codecrafts.services;

public class LoginService {
    public boolean validateUser(String user, String password) {
        return user.equalsIgnoreCase("abc") && password.equals("123");
    }
}