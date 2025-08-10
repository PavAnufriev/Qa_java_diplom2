package ru.yandex.praktikum.stellarburgers.models;

public class OrderResponse {
    public boolean success;
    public String name;
    public Order order;

    public static class Order {
        public int number;
    }
}
