package ru.yandex.praktikum.stellarburgers.models;

public class User {
    public String email;
    public String password;
    public String name;

    public User() {}

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User random() {
        String suffix = String.valueOf(System.currentTimeMillis());
        return new User("user" + suffix + "@example.com", "Passw0rd!", "User" + suffix);
    }
}
