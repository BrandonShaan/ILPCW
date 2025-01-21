package com.example.cw1;

public class LngLatPairRequest {

    private Position position1;
    private Position position2;

    // Getters and Setters
    public Position getPosition1() {
        return position1;
    }

    public void setPosition1(Position position1) {
        this.position1 = position1;
    }

    public Position getPosition2() {
        return position2;
    }

    public void setPosition2(Position position2) {
        this.position2 = position2;
    }

    // Inner class to represent a coordinate pair
    public static class Position {
        private Double lng;  // Changed from 'double' to 'Double' to allow null values
        private Double lat;  // Changed from 'double' to 'Double' to allow null values

        // Getters and Setters
        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }
    }
}