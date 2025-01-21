package com.example.cw1;

import java.util.List;

public class CentralArea {
    private List<LngLat> vertices;

    public List<LngLat> getVertices() {
        return vertices;
    }

    public void setVertices(List<LngLat> vertices) {
        this.vertices = vertices;
    }

    public boolean contains(LngLat point) {
        int n = vertices.size();
        int intersections = 0;

        for (int i = 0; i < n; i++) {
            LngLat v1 = vertices.get(i);
            LngLat v2 = vertices.get((i + 1) % n);

            if (rayIntersectsSegment(point, v1, v2)) {
                intersections++;
            }
        }
        return intersections % 2 == 1;
    }

    private boolean rayIntersectsSegment(LngLat p, LngLat v1, LngLat v2) {
        if (v1.getLat() > v2.getLat()) {
            LngLat temp = v1;
            v1 = v2;
            v2 = temp;
        }

        if (p.getLat() == v1.getLat() || p.getLat() == v2.getLat()) {
            p = new LngLat(p.getLng(), p.getLat() + 0.0000001);
        }

        if (p.getLat() < v1.getLat() || p.getLat() > v2.getLat() || p.getLng() >= Math.max(v1.getLng(), v2.getLng())) {
            return false;
        }

        if (p.getLng() < Math.min(v1.getLng(), v2.getLng())) {
            return true;
        }

        double slope = (v2.getLng() - v1.getLng()) / (v2.getLat() - v1.getLat());
        double intersectLng = v1.getLng() + (p.getLat() - v1.getLat()) * slope;
        return p.getLng() < intersectLng;
    }
}



