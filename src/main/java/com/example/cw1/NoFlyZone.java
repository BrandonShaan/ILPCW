package com.example.cw1;

import java.util.List;

public class NoFlyZone {
    private String name;
    private List<LngLat> vertices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LngLat> getVertices() {
        return vertices;
    }

    public void setVertices(List<LngLat> vertices) {
        this.vertices = vertices;
    }

    public boolean intersects(LngLat start, LngLat end) {
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            LngLat v1 = vertices.get(i);
            LngLat v2 = vertices.get((i + 1) % n);

            if (segmentsIntersect(start, end, v1, v2)) {
                return true;
            }
        }
        return false;
    }

    private boolean segmentsIntersect(LngLat p1, LngLat p2, LngLat q1, LngLat q2) {
        double d1 = calculateDirection(q1, q2, p1);
        double d2 = calculateDirection(q1, q2, p2);
        double d3 = calculateDirection(p1, p2, q1);
        double d4 = calculateDirection(p1, p2, q2);

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        }

        return (d1 == 0 && onSegment(q1, q2, p1)) ||
                (d2 == 0 && onSegment(q1, q2, p2)) ||
                (d3 == 0 && onSegment(p1, p2, q1)) ||
                (d4 == 0 && onSegment(p1, p2, q2));
    }

    private double calculateDirection(LngLat pi, LngLat pj, LngLat pk) {
        return (pk.getLng() - pi.getLng()) * (pj.getLat() - pi.getLat()) -
                (pj.getLng() - pi.getLng()) * (pk.getLat() - pi.getLat());
    }

    private boolean onSegment(LngLat pi, LngLat pj, LngLat pk) {
        return Math.min(pi.getLng(), pj.getLng()) <= pk.getLng() &&
                pk.getLng() <= Math.max(pi.getLng(), pj.getLng()) &&
                Math.min(pi.getLat(), pj.getLat()) <= pk.getLat() &&
                pk.getLat() <= Math.max(pi.getLat(), pj.getLat());
    }
}
