package com.example.cw1;

public class NextPositionRequest {

    private Position start; // Starting position
    private Double angle;   // Angle in degrees

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public static class Position {
        private Double lng; // Change to Double to allow null
        private Double lat; // Change to Double to allow null

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