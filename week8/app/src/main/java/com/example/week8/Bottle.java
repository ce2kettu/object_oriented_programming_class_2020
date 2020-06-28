package com.example.week8;

enum BottleSize {
    SMALL,
    NORMAL,
    BIG
}

public class Bottle {
    private String name;
    private String manufacturer;
    private float total_energy;
    private BottleSize size;
    private float price;

    public Bottle() {
        name = "Pepsi Max";
        manufacturer = "Pepsi";
        total_energy = 0.3f;
        size = BottleSize.NORMAL;
        price = 1.80f;
    }

    public Bottle(String name, String manufacturer, float totE, BottleSize size, float price) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.manufacturer = manufacturer;
        this.total_energy = totE;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public float getEnergy() {
        return total_energy;
    }

    public float getPrice() {
        return price;
    }

    public BottleSize getSize() {
        return size;
    }
}