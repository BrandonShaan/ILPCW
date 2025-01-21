package com.example.cw1.model;

import java.util.List;

public class Restaurant {
    private String name;
    private Location location;
    private List<String> openingDays;
    private List<MenuItem> menu;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(List<String> openingDays) {
        this.openingDays = openingDays;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }
}

