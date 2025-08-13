package ru.yandex.praktikum.stellarburgers.models;

import java.util.Random;

public class User {
    public String email;
    public String password;
    public String name;

    private static final Random random = new Random();

    public User() {}

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User random() {
        String suffix = String.valueOf(System.currentTimeMillis() + random.nextInt(1000));
        return new User(
            "user" + suffix + "@example.com",
            "password" + suffix,
            "User" + suffix
        );
    }
}
