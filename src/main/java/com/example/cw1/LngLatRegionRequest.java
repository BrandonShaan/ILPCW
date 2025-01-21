package com.example.cw1;

public class LngLatRegionRequest {

    private Position position;
    private Region region;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public static class Position {
        private double lng;
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Region {
        private String name;
        private Position[] vertices;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Position[] getVertices() {
            return vertices;
        }

        public void setVertices(Position[] vertices) {
            this.vertices = vertices;
        }
    }
}