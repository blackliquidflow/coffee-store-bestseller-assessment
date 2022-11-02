# Getting Started

###
H2 in-memory database is used for testing purposes.
Web interface URL: http://localhost:8080/h2-console/
JDBC URL setting: jdbc:h2:mem:coffee-store-db

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.5/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.5/reference/htmlsingle/#using.devtools)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

# Functionality Description

Note: Drink with Toppings is referred to as Beverage

1) DrinksController is used for create, update, delete and search operations with Drinks.
2) ToppingsController is used for create, update, delete and search operations with Toppings.
3) CartController provides the following functionality for Shopping Cart: adding Beverage to Cart; get all
   Beverages present in the Cart; delete one Beverage from the Cart; clear the Cart.
4) OrdersController provides the functionality of creating an Order from the contents of Shopping Cart.
5) ReportsController provides data based on Order History records: getting most used Toppings for designated
   time period.

# Some Notes Regarding Implementation Decisions

1) I've considered storing Drinks and Toppings in one table. It would merge very similar logic that is present for
   fetching/creating/updating/deleting these objects. But this would make the solution less extensible and also make
   search operations in the resulting table slower.
2) Models are used directly in API responses instead of DTOs to lessen boilerplate code a bit. Since
   no sensitive information is present in the models that is not intended to be sent to the client it seems ok to do so.
3) Mechanism of calculating the total price for Cart and Order is simplified. In real application it should contain
   either invalidation of cart items when the price changes or re-calculation of total order price during the order
   creation step.
4) I've decided to go with a separate table to store order history to make searching data for reports easier and also
   make operations with the main Order table faster as it now can be cleared from time to time.
5) Also, worth splitting packages to logical subsections - like 'ordering', 'reports', 'items_management' when the
   codebase grows.

# Run Docker Container
1) docker build -t coffee-store .
2) docker run -d -p 8080:8080 {image_id}
