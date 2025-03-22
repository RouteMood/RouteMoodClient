package ru.hse.routemood;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionManager {

    private static SessionManager instance;
    private String token;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
}
