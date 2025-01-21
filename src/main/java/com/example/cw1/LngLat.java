package com.example.cw1;

public class LngLat {
    private final double lng;
    private final double lat;

    public LngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double distanceTo(LngLat other) {
        double dLng = this.lng - other.getLng();
        double dLat = this.lat - other.getLat();
        return Math.sqrt(dLng * dLng + dLat * dLat);
    }


    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public boolean closeTo(LngLat other) {
        return Math.abs(this.lng - other.lng) < 0.00015 && Math.abs(this.lat - other.lat) < 0.00015;
    }

    public LngLat move(CompassDirection direction, double stepSize) {
        double rad = Math.toRadians(direction.getAngle());
        double newLng = this.lng + stepSize * Math.cos(rad);
        double newLat = this.lat + stepSize * Math.sin(rad);
        return new LngLat(newLng, newLat);
    }
/*
    public CompassDirection nextStep(LngLat target) {
        double angle = Math.atan2(target.getLat() - this.lat, target.getLng() - this.lng);
        return CompassDirection.fromAngle(Math.toDegrees(angle));
    }

 */

    public CompassDirection nextStep(LngLat target) {
        double angle = Math.atan2(target.getLat() - this.getLat(), target.getLng() - this.getLng());
        CompassDirection direction = CompassDirection.fromAngle(Math.toDegrees(angle));


        return direction;
    }



    @Override
    public String toString() {
        return "LngLat{" + "lng=" + lng + ", lat=" + lat + '}';
    }
}




