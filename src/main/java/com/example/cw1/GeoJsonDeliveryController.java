package com.example.cw1;

import com.example.cw1.DeliveryController;
import com.example.cw1.LngLat;
import com.example.cw1.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class GeoJsonDeliveryController {
    private final DeliveryController deliveryController;

    public GeoJsonDeliveryController(DeliveryController deliveryController) {
        this.deliveryController = deliveryController;
    }

    @PostMapping("/calcDeliveryPathAsGeoJson")
    public ResponseEntity<String> calcDeliveryPathAsGeoJson(@RequestBody Order order) {
        ResponseEntity<List<LngLat>> deliveryPathResponse = deliveryController.calcDeliveryPath(order);

        if (!deliveryPathResponse.getStatusCode().is2xxSuccessful() || deliveryPathResponse.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<LngLat> deliveryPath = deliveryPathResponse.getBody();


        String geoJson = convertPathToGeoJson(deliveryPath);

        return ResponseEntity.ok(geoJson);
    }

    private String convertPathToGeoJson(List<LngLat> path) {
        StringBuilder geoJson = new StringBuilder();
        geoJson.append("{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",")
                .append("\"geometry\":{\"type\":\"LineString\",\"coordinates\":[");

        for (int i = 0; i < path.size(); i++) {
            LngLat point = path.get(i);
            geoJson.append("[").append(point.getLng()).append(",").append(point.getLat()).append("]");
            if (i < path.size() - 1) {
                geoJson.append(",");
            }
        }

        geoJson.append("]},\"properties\":{}}]}");
        return geoJson.toString();
    }
}
