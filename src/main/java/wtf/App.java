package wtf;

import wtf.model.ChatApplication;
import wtf.service.Application;

public class App {

    public static void main(String[] args) {
        new Application(new ChatApplication()).start();
    }
}
