package com.guilherme.inventorycontrol;

import android.graphics.Bitmap;

public class Product {
    private int id;
    private String name;
    private String description;
    private Bitmap image;

    public Product(int id, String name, String description, Bitmap image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }
}

