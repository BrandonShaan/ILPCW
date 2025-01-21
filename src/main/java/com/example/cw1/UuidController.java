package com.example.cw1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
public class UuidController {

    private static final String STUDENT_ID = "s2332329";
    private static final double DRONE_IS_CLOSE_DISTANCE = 0.00015;

    @GetMapping("/uuid")
    public String getUuid() {
        return STUDENT_ID;
    }

    @PostMapping("/distanceTo")
    public ResponseEntity<Double> calculateDistance(@RequestBody LngLatPairRequest request) {
        try {
            LngLatPairRequest.Position position1 = request.getPosition1();
            LngLatPairRequest.Position position2 = request.getPosition2();

            if (!isValidCoordinate(position1) || !isValidCoordinate(position2)) {
                return ResponseEntity.badRequest().build();
            }

            double distance = calculateEuclideanDistance(
                    position1.getLat(), position1.getLng(),
                    position2.getLat(), position2.getLng()
            );

            return ResponseEntity.ok(distance);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request) {
        if (request == null || request.getPosition1() == null || request.getPosition2() == null) {
            return ResponseEntity.badRequest().build();
        }

        LngLatPairRequest.Position position1 = request.getPosition1();
        LngLatPairRequest.Position position2 = request.getPosition2();

        if (!isValidCoordinate(position1) || !isValidCoordinate(position2)) {
            return ResponseEntity.badRequest().build();
        }

        double distance = calculateEuclideanDistance(
                position1.getLat(), position1.getLng(),
                position2.getLat(), position2.getLng()
        );

        boolean isClose = distance < DRONE_IS_CLOSE_DISTANCE;

        return ResponseEntity.ok(isClose);
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<?> getNextPosition(@RequestBody NextPositionRequest request) {
        if (request == null || request.getStart() == null) {
            return ResponseEntity.badRequest().build();
        }

        NextPositionRequest.Position start = request.getStart();
        if (!isValidCoordinate(start)) {
            return ResponseEntity.badRequest().build();
        }

        if (request.getAngle() == null || !isValidAngle(request.getAngle())) {
            return ResponseEntity.badRequest().build();
        }

        double angleInRadians = Math.toRadians(request.getAngle());
        double step = 0.00015;

        double newLat = start.getLat() + step * Math.sin(angleInRadians);
        double newLng = start.getLng() + step * Math.cos(angleInRadians);

        NextPositionResponse nextPosition = new NextPositionResponse(newLng, newLat);

        return ResponseEntity.ok(nextPosition);
    }

    @PostMapping("/isInRegion")
    public ResponseEntity<?> isInRegion(@RequestBody LngLatRegionRequest request) {

        if (request == null || request.getPosition() == null || request.getRegion() == null || request.getRegion().getVertices() == null) {
            return ResponseEntity.badRequest().build();
        }

        LngLatRegionRequest.Position position = request.getPosition();
        LngLatRegionRequest.Position[] vertices = request.getRegion().getVertices();


        if (vertices.length < 4) {
            return ResponseEntity.badRequest().build();
        }


        if (!isValidCoordinate(position)) {
            return ResponseEntity.badRequest().build();
        }


        for (LngLatRegionRequest.Position vertex : vertices) {
            if (!isValidCoordinate(vertex)) {
                return ResponseEntity.badRequest().build();
            }
        }


        if (!areVerticesClosed(vertices)) {
            return ResponseEntity.badRequest().build();
        }


        boolean isInRegion = isPointInPolygon(position, vertices);
        boolean isClose = isPointCloseToRegion(position, vertices);

        return ResponseEntity.ok(isInRegion || isClose);
    }

    private boolean areVerticesClosed(LngLatRegionRequest.Position[] vertices) {
        LngLatRegionRequest.Position firstVertex = vertices[0];
        LngLatRegionRequest.Position lastVertex = vertices[vertices.length - 1];

        return firstVertex.getLat() == lastVertex.getLat() && firstVertex.getLng() == lastVertex.getLng();
    }

    private boolean isValidCoordinate(Object position) {
        Double lat = null;
        Double lng = null;

        if (position instanceof LngLatRegionRequest.Position) {
            LngLatRegionRequest.Position pos = (LngLatRegionRequest.Position) position;
            lat = pos.getLat();
            lng = pos.getLng();
        } else if (position instanceof NextPositionRequest.Position) {
            NextPositionRequest.Position pos = (NextPositionRequest.Position) position;
            lat = pos.getLat();
            lng = pos.getLng();
        } else if (position instanceof LngLatPairRequest.Position) {
            LngLatPairRequest.Position pos = (LngLatPairRequest.Position) position;
            lat = pos.getLat();
            lng = pos.getLng();
        }

        return lat != null && lng != null && lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }

    private boolean isValidAngle(Double angle) {
        return angle != null && angle >= 0 && angle <= 360;
    }

    private boolean isPointInPolygon(LngLatRegionRequest.Position point, LngLatRegionRequest.Position[] vertices) {
        int numVertices = vertices.length;
        boolean inside = false;

        double x = point.getLng();
        double y = point.getLat();

        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            double xi = vertices[i].getLng(), yi = vertices[i].getLat();
            double xj = vertices[j].getLng(), yj = vertices[j].getLat();

            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }

    private boolean isPointCloseToRegion(LngLatRegionRequest.Position position, LngLatRegionRequest.Position[] vertices) {
        for (LngLatRegionRequest.Position vertex : vertices) {
            double distance = calculateEuclideanDistance(position.getLat(), position.getLng(), vertex.getLat(), vertex.getLng());
            if (distance < DRONE_IS_CLOSE_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    private double calculateEuclideanDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = lat2 - lat1;
        double dLng = lng2 - lng1;
        return Math.sqrt(dLat * dLat + dLng * dLng);
    }



}