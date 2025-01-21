/*

package com.example.cw1;



import com.example.cw1.enums.OrderStatus;
import com.example.cw1.enums.OrderValidationCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class OrderValidation {

    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidationResult> validateOrder(@RequestBody Order order) {
        OrderValidationResult result = validate(order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public OrderValidationResult validate(Order order) {
        // Validate order date
        try {
            LocalDate orderDate = LocalDate.parse(order.getOrderDate());
            if (orderDate.isBefore(LocalDate.now())) {
                return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
            }
        } catch (DateTimeParseException e) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Validate credit card expiry
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth creditCardExpiry = YearMonth.parse(order.getCreditCardInformation().getCreditCardExpiry(), formatter);
            YearMonth currentMonthYear = YearMonth.now();

            if (creditCardExpiry.isBefore(currentMonthYear)) {
                return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
            }
        } catch (DateTimeParseException e) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Fetch restaurants
        List<Restaurant> restaurants = fetchRestaurants();
        if (restaurants == null) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.UNDEFINED);
        }

        // Validate pizza count
        if (order.getPizzasInOrder().isEmpty()) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EMPTY_ORDER);
        }

        if (order.getPizzasInOrder().size() > 4) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
        }

        // Validate pizzas and prices
        boolean pizzaNotDefined = order.getPizzasInOrder().stream()
                .anyMatch(pizza -> !isPizzaDefined(pizza, restaurants));
        if (pizzaNotDefined) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_NOT_DEFINED);
        }

        boolean priceInvalid = order.getPizzasInOrder().stream()
                .anyMatch(pizza -> !isPizzaPriceValid(pizza, restaurants));
        if (priceInvalid) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
        }

        // Find matching restaurant
        Restaurant matchingRestaurant = findMatchingRestaurant(order, restaurants);
        if (matchingRestaurant == null) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }

        // Validate restaurant opening days
        LocalDate orderDate = LocalDate.parse(order.getOrderDate());
        DayOfWeek orderDayOfWeek = orderDate.getDayOfWeek();
        boolean isOpen = matchingRestaurant.getOpeningDays().stream()
                .anyMatch(day -> day.equalsIgnoreCase(orderDayOfWeek.name()));
        if (!isOpen) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.RESTAURANT_CLOSED);
        }

        // Validate total price
        int totalPrice = order.getPizzasInOrder().stream().mapToInt(Pizza::getPriceInPence).sum();
        if (totalPrice != order.getPriceTotalInPence() - 100) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);
        }

        // Validate credit card information
        if (!isValidCreditCard(order.getCreditCardInformation())) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.CARD_NUMBER_INVALID);
        }

        // Validate CVV length (if the CVV is incorrect, return CVV_INVALID)
        if (!Pattern.matches("^\\d{3}$", order.getCreditCardInformation().getCvv())) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.CVV_INVALID);
        }

        return new OrderValidationResult(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
    }

    // Validate credit card: 16 digits and CVV: 3 digits
    private boolean isValidCreditCard(CreditCardInformation creditCardInformation) {
        String cardNumber = creditCardInformation.getCreditCardNumber().replaceAll(" ", "");
        return Pattern.matches("^\\d{16}$", cardNumber) &&
                Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", creditCardInformation.getCreditCardExpiry());
    }

    // Fetch restaurant data from the external API
    public List<Restaurant> fetchRestaurants() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Restaurant[]> response = restTemplate.getForEntity(
                    "https://ilp-rest-2024.azurewebsites.net/restaurants",
                    Restaurant[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
        } catch (Exception e) {
            System.err.println("Error fetching restaurant data: " + e.getMessage());
            return null;
        }
    }

    // Check if the pizza is defined in the restaurant's menu
    public boolean isPizzaDefined(Pizza pizza, List<Restaurant> restaurants) {
        return restaurants.stream()
                .anyMatch(restaurant -> restaurant.getMenu().stream()
                        .anyMatch(menuItem -> stripPrefix(menuItem.getName())
                                .equalsIgnoreCase(stripPrefix(pizza.getName()))));
    }

    // Check if the pizza price is valid in the restaurant's menu
    public boolean isPizzaPriceValid(Pizza pizza, List<Restaurant> restaurants) {
        return restaurants.stream()
                .anyMatch(restaurant -> restaurant.getMenu().stream()
                        .anyMatch(menuItem -> stripPrefix(menuItem.getName())
                                .equalsIgnoreCase(stripPrefix(pizza.getName()))
                                && menuItem.getPriceInPence() == pizza.getPriceInPence()));
    }

    // Find the matching restaurant based on the pizzas in the order
    public Restaurant findMatchingRestaurant(Order order, List<Restaurant> restaurants) {
        return restaurants.stream()
                .filter(restaurant -> order.getPizzasInOrder().stream()
                        .allMatch(pizza -> restaurant.getMenu().stream()
                                .anyMatch(menuItem -> stripPrefix(menuItem.getName())
                                        .equalsIgnoreCase(stripPrefix(pizza.getName()))
                                        && menuItem.getPriceInPence() == pizza.getPriceInPence())))
                .findFirst()
                .orElse(null);
    }


    // Remove prefix from the pizza name for comparison
    public String stripPrefix(String name) {
        if (name.contains(":")) {
            return name.split(":", 2)[1].trim();
        }
        return name.trim();
    }


}
*/



package com.example.cw1;

import com.example.cw1.enums.OrderStatus;
import com.example.cw1.enums.OrderValidationCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class OrderValidation {

    @PostMapping("/validateOrder")
    public ResponseEntity<OrderValidationResult> validateOrder(@RequestBody Order order) {
        OrderValidationResult result = validate(order);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public OrderValidationResult validate(Order order) {

        try {
            LocalDate orderDate = LocalDate.parse(order.getOrderDate());
            if (orderDate.isBefore(LocalDate.now())) {
                return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
            }
        } catch (DateTimeParseException e) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Validate credit card expiry
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth creditCardExpiry = YearMonth.parse(order.getCreditCardInformation().getCreditCardExpiry(), formatter);
            YearMonth currentMonthYear = YearMonth.now();

            if (creditCardExpiry.isBefore(currentMonthYear)) {
                return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
            }
        } catch (DateTimeParseException e) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EXPIRY_DATE_INVALID);
        }

        // Fetch restaurants
        List<Restaurant> restaurants = fetchRestaurants();
        if (restaurants == null) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.UNDEFINED);
        }

        // Validate pizza count
        if (order.getPizzasInOrder().isEmpty()) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.EMPTY_ORDER);
        }

        if (order.getPizzasInOrder().size() > 4) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
        }

        // Validate pizzas and prices
        boolean pizzaNotDefined = order.getPizzasInOrder().stream()
                .anyMatch(pizza -> !isPizzaDefined(pizza, restaurants));
        if (pizzaNotDefined) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_NOT_DEFINED);
        }

        boolean priceInvalid = order.getPizzasInOrder().stream()
                .anyMatch(pizza -> !isPizzaPriceValid(pizza, restaurants));
        if (priceInvalid) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
        }

        // Find matching restaurant
        Restaurant matchingRestaurant = findMatchingRestaurant(order, restaurants);
        if (matchingRestaurant == null) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
        }

        // Validate restaurant opening days
        LocalDate orderDate = LocalDate.parse(order.getOrderDate());
        DayOfWeek orderDayOfWeek = orderDate.getDayOfWeek();
        boolean isOpen = matchingRestaurant.getOpeningDays().stream()
                .anyMatch(day -> day.equalsIgnoreCase(orderDayOfWeek.name()));
        if (!isOpen) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.RESTAURANT_CLOSED);
        }

        // Validate total price
        int totalPrice = order.getPizzasInOrder().stream().mapToInt(Pizza::getPriceInPence).sum();
        if (totalPrice != order.getPriceTotalInPence() - 100) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.TOTAL_INCORRECT);
        }

        // Validate credit card information
        if (!isValidCreditCard(order.getCreditCardInformation())) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.CARD_NUMBER_INVALID);
        }

        // Validate CVV length (if the CVV is incorrect, return CVV_INVALID)
        if (!Pattern.matches("^\\d{3}$", order.getCreditCardInformation().getCvv())) {
            return new OrderValidationResult(OrderStatus.INVALID, OrderValidationCode.CVV_INVALID);
        }

        return new OrderValidationResult(OrderStatus.VALID, OrderValidationCode.NO_ERROR);
    }

    // Validate credit card: 16 digits and CVV: 3 digits
    private boolean isValidCreditCard(CreditCardInformation creditCardInformation) {
        String cardNumber = creditCardInformation.getCreditCardNumber().replaceAll(" ", "");
        return Pattern.matches("^\\d{16}$", cardNumber) &&
                Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", creditCardInformation.getCreditCardExpiry());
    }

    // Fetch restaurant data from the external API
    public List<Restaurant> fetchRestaurants() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Restaurant[]> response = restTemplate.getForEntity(
                    "https://ilp-rest-2024.azurewebsites.net/restaurants",
                    Restaurant[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
        } catch (Exception e) {
            System.err.println("Error fetching restaurant data: " + e.getMessage());
            return null;
        }
    }

    // Check if the pizza is defined in the restaurant's menu
    public boolean isPizzaDefined(Pizza pizza, List<Restaurant> restaurants) {
        return restaurants.stream()
                .anyMatch(restaurant -> restaurant.getMenu().stream()
                        .anyMatch(menuItem -> menuItem.getName()
                                .equalsIgnoreCase (pizza.getName())));
    }

    // Check if the pizza price is valid in the restaurant's menu
    public boolean isPizzaPriceValid(Pizza pizza, List<Restaurant> restaurants) {
        return restaurants.stream()
                .anyMatch(restaurant -> restaurant.getMenu().stream()
                        .anyMatch(menuItem -> menuItem.getName()
                                .equalsIgnoreCase(pizza.getName())
                                && menuItem.getPriceInPence() == pizza.getPriceInPence()));
    }

    // Find the matching restaurant based on the pizzas in the order
    public Restaurant findMatchingRestaurant(Order order, List<Restaurant> restaurants) {
        return restaurants.stream()
                .filter(restaurant -> order.getPizzasInOrder().stream()
                        .allMatch(pizza -> restaurant.getMenu().stream()
                                .anyMatch(menuItem -> menuItem.getName()
                                        .equalsIgnoreCase(pizza.getName())
                                        && menuItem.getPriceInPence() == pizza.getPriceInPence())))
                .findFirst()
                .orElse(null);
    }





}
