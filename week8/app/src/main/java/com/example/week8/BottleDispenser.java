package com.example.week8;

import android.util.Log;

import java.util.ArrayList;

public class BottleDispenser {
    public class BuyResult {
        boolean success = false;
        Bottle bottle = null;
        String message;
    }

    private static BottleDispenser singleton_instance = null;

    private ArrayList<Bottle> bottle_array;
    private double money;

    public static BottleDispenser getInstance() {
        if (singleton_instance == null) {
            singleton_instance = new BottleDispenser();
        }
        return singleton_instance;
    }

    public BottleDispenser() {
        money = 0;

        // Initialize the array
        bottle_array = new ArrayList<Bottle>();

        bottle_array.add(new Bottle());
        bottle_array.add(new Bottle("Pepsi Max", "Pepsi", 0.3f, BottleSize.NORMAL, 2.2f));
        bottle_array.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3f, BottleSize.BIG, 2.0f));
        bottle_array.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3f, BottleSize.SMALL, 2.5f));
        bottle_array.add(new Bottle("Fanta Zero", "Fanta", 0.3f, BottleSize.SMALL, 1.95f));
    }

    public String addMoney(int amount) {
        money += amount;
        return "Klink! Added more money!";
    }

    private Bottle findBottle(String type, BottleSize size) {
        for (Bottle bottle : bottle_array) {
            if (bottle.getName().equals(type) && bottle.getSize() == size) {
                return bottle;
            }
        }

        return null;
    }

    public BuyResult buyBottle(String type, BottleSize size) {
        Bottle bottle = findBottle(type, size);
        BuyResult result = new BuyResult();

        if (bottle == null) {
            result.message = "No such soda available";
            return result;
        }

        if (money < bottle.getPrice()) {
            result.message = "Add money first";
            return result;
        }

        money -= bottle.getPrice();
        bottle_array.remove(bottle);
        result.message = "KACHUNK! " + bottle.getName() + " came out of the dispenser!";
        result.success = true;
        result.bottle = bottle;
        return result;
    }

    public String returnMoney() {
        double oldMoney = money;
        money = 0;
        return String.format("Klink klink. Money came out! You got %.2f€ back\n", oldMoney);
    }


}