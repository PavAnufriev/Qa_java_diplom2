package ru.yandex.praktikum.stellarburgers.models;

public class RegisterResponse {
    public boolean success;
    public Tokens tokens;
    public User user;

    public static class Tokens {
        public String accessToken; // "Bearer ..."
        public String refreshToken;
    }
}
