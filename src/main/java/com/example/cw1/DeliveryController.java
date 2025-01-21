package com.example.cw1;

import com.example.cw1.enums.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DeliveryController {
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    @PostMapping("/calcDeliveryPath")
    public ResponseEntity<List<LngLat>> calcDeliveryPath(@RequestBody Order order) {
        OrderValidation validator = new OrderValidation();
        OrderValidationResult validationResult = validator.validate(order);

        if (!validationResult.getOrderStatus().equals(OrderStatus.VALID)) {
            return ResponseEntity.badRequest().build();
        }

        List<Restaurant> restaurants = validator.fetchRestaurants();
        Restaurant restaurant = validator.findMatchingRestaurant(order, restaurants);
        if (restaurant == null) {
            return ResponseEntity.badRequest().build();
        }

        LngLat restaurantLocation = new LngLat(
                restaurant.getLocation().getLng(),
                restaurant.getLocation().getLat()
        );

        CentralArea centralArea = fetchCentralArea();
        List<NoFlyZone> noFlyZones = fetchNoFlyZones();

        if (centralArea == null || noFlyZones == null) {
            return ResponseEntity.status(500).build();
        }

        List<LngLat> path = calculatePath(restaurantLocation, APPLETON_TOWER, centralArea, noFlyZones);
        return ResponseEntity.ok(path);
    }

    private CentralArea fetchCentralArea() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CentralArea> response = restTemplate.getForEntity(
                    "https://ilp-rest-2024.azurewebsites.net/centralArea",
                    CentralArea.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private List<NoFlyZone> fetchNoFlyZones() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<NoFlyZone[]> response = restTemplate.getForEntity(
                    "https://ilp-rest-2024.azurewebsites.net/noFlyZones",
                    NoFlyZone[].class
            );
            return response.getBody() != null ? List.of(response.getBody()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<LngLat> calculatePath(LngLat start, LngLat end, CentralArea centralArea, List<NoFlyZone> noFlyZones) {
        List<LngLat> path = new ArrayList<>();
        LngLat currentPosition = start;

        while (!currentPosition.closeTo(end)) {
            CompassDirection direction = currentPosition.nextStep(end);
            LngLat nextPosition = currentPosition.move(direction, 0.00015);

            if (!intersectsNoFlyZone(currentPosition, nextPosition, noFlyZones)) {
                path.add(nextPosition);
                currentPosition = nextPosition;
                continue;
            }

            direction = adjustDirection(currentPosition, noFlyZones);
            nextPosition = currentPosition.move(direction, 0.00015);

            path.add(nextPosition);
            currentPosition = nextPosition;
        }

        return path;
    }

    private CompassDirection adjustDirection(LngLat currentPosition, List<NoFlyZone> noFlyZones) {
        CompassDirection bestDirection = null;

        for (CompassDirection direction : CompassDirection.values()) {
            LngLat testPosition = currentPosition.move(direction, 0.00015);
            if (!intersectsNoFlyZone(currentPosition, testPosition, noFlyZones)) {
                bestDirection = direction;
                break;
            }
        }

        return (bestDirection != null) ? bestDirection : CompassDirection.EAST;
    }


    private boolean intersectsNoFlyZone(LngLat start, LngLat end, List<NoFlyZone> noFlyZones) {
        for (NoFlyZone zone : noFlyZones) {
            if (zone.intersects(start, end)) {
                return true;
            }
        }
        return false;
    }


}
