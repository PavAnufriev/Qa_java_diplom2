package ru.yandex.praktikum.stellarburgers.models;

public class LoginRequest {
    public String email;
    public String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
