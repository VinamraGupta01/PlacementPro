package com.placement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Smart Placement & Internship Management System
 * ------------------------------------------------
 * Built with Spring Boot, JDBC, MySQL, JWT, Bootstrap.
 *
 * Open:
 *   - Frontend  -> http://localhost:8080/
 *   - Swagger   -> http://localhost:8080/swagger-ui.html
 */
@SpringBootApplication
public class PlacementApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlacementApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println(" Smart Placement Management System is RUNNING ");
        System.out.println(" Frontend : http://localhost:8080/");
        System.out.println(" Swagger  : http://localhost:8080/swagger-ui.html");
        System.out.println("==============================================\n");
    }
}
