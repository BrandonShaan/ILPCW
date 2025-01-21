package com.example.cw1;

public class NextPositionResponse {
    private double lng;
    private double lat;

    public NextPositionResponse(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }
}