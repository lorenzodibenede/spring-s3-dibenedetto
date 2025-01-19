package ch.hearc.jee2024.tourismapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TourismApiApplication {

    public static void main(String[] args) {

        System.out.println("Before Start");
        SpringApplication.run(TourismApiApplication.class, args);
        System.out.println("After Start");
    }

}
