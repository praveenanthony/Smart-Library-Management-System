package models;

import patterns.observer.Observer;

public class Notification implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[Notification] " + message);
    }
}