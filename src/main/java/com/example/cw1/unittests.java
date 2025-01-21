package com.example.cw1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class unittests {

    private static final String ORDERS_URL = "https://ilp-rest-2024.azurewebsites.net/orders"; // URL to fetch orders from

    public static void main(String[] args) {
        // Fetch orders from the provided URL
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order[]> response = restTemplate.getForEntity(ORDERS_URL, Order[].class);
        List<Order> orders = Arrays.asList(response.getBody());

        // Create an instance of the OrderValidation class
        OrderValidation orderValidation = new OrderValidation();

        // Initialize counters and a list for failed tests
        int passedTests = 0;
        int failedTests = 0;
        List<String> failedOrderNumbers = new ArrayList<>();

        // Validate each order and compare with the expected validation code
        for (Order order : orders) {
            // Get the validation result from OrderValidation
            OrderValidationResult validationResult = orderValidation.validate(order);
            String actualOrderStatus = validationResult.getOrderStatus().name(); // VALID or INVALID
            String actualValidationCode = validationResult.getOrderValidationCode().name(); // Specific error code or NO_ERROR

            // Get the expected validation code from the test data
            String expectedOrderStatus = order.getOrderStatus(); // Expected: VALID or INVALID
            String expectedValidationCode = order.getOrderValidationCode(); // Expected specific validation code (e.g., EXPIRY_DATE_INVALID)

            // Print the order number, actual and expected validation codes
            System.out.println("Order No: " + order.getOrderNo());
            System.out.println("Actual Order Status: " + actualOrderStatus);
            System.out.println("Expected Order Status: " + expectedOrderStatus);
            System.out.println("Actual Validation Code: " + actualValidationCode);
            System.out.println("Expected Validation Code: " + expectedValidationCode);

            // Check order status comparison
            boolean isOrderStatusCorrect = actualOrderStatus.equals(expectedOrderStatus);
            boolean isValidationCodeCorrect = actualValidationCode.equals(expectedValidationCode);

            // If both the order status and validation code are correct, consider it passed
            if (isOrderStatusCorrect && isValidationCodeCorrect) {
                System.out.println("Order Status and Validation Code Test Passed!");
                passedTests++;
            } else {
                // Print the full test details if the test fails
                System.out.println("Test Failed!");
                System.out.println("Failing Order Details:");
                System.out.println("Order No: " + order.getOrderNo());
                System.out.println("Order Date: " + order.getOrderDate());
                System.out.println("Order Status (Actual): " + actualOrderStatus);
                System.out.println("Order Status (Expected): " + expectedOrderStatus);
                System.out.println("Validation Code (Actual): " + actualValidationCode);
                System.out.println("Validation Code (Expected): " + expectedValidationCode);
                System.out.println("----");

                // Store failed test order number
                failedTests++;
                failedOrderNumbers.add(order.getOrderNo());
            }

            System.out.println("----");
        }

        // Print the final test summary
        System.out.println("Test Summary:");
        System.out.println("Tests Passed: " + passedTests);
        System.out.println("Tests Failed: " + failedTests);

        // Print the list of failed order numbers
        if (failedTests > 0) {
            System.out.println("Failed Orders: ");
            for (String failedOrder : failedOrderNumbers) {
                System.out.println(failedOrder);
            }
        } else {
            System.out.println("All tests passed!");
        }

        // Optionally return the failed order numbers as a result
        if (!failedOrderNumbers.isEmpty()) {
            System.out.println("Failed Order Numbers: " + failedOrderNumbers);
        }
    }
}
