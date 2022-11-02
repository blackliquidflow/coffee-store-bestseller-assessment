package com.coffeestore.enums;

public enum IngredientType {
    DRINK("drink"), TOPPING("topping");

    private final String value;

    IngredientType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
