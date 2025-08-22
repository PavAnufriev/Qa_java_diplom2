package ru.yandex.praktikum.stellarburgers.models;

import java.util.List;

public class OrderRequest {
    public List<String> ingredients;

    public OrderRequest() {}

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
