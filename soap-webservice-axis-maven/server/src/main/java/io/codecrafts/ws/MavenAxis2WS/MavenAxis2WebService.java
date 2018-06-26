package io.codecrafts.ws.MavenAxis2WS;

public class MavenAxis2WebService {
    public String ping(String text) {
        if (text == null) {
            return "Service is up and available";
        }
        return "Service is up and available, message: " + text;
    }

    public int sum(int x, int y) {
        return x + y;
    }
}