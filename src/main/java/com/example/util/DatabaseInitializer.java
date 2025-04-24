package com.example.util;

import com.example.model.Car;
import com.example.model.User;
import com.example.service.CarService;
import com.example.service.RentalService;
import com.example.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.math.BigDecimal;

@Singleton
@Startup
public class DatabaseInitializer {

    @Inject
    private UserService userService;

    @Inject
    private CarService carService;

    @Inject
    private RentalService rentalService;

    @PostConstruct
    public void init() {
        // Проверяем, есть ли уже данные
        if (userService.findAll().isEmpty()) {
            createUsers();
            createCars();
        }
    }

    private void createUsers() {
        // Создаем менеджера
        User manager = new User("Админ", "admin@example.com", "+7 (999) 123-45-67", "admin", "MANAGER");
        userService.save(manager);

        // Создаем клиентов
        User client1 = new User("Иван Иванов", "ivan@example.com", "+7 (999) 111-22-33", "password", "CLIENT");
        User client2 = new User("Мария Петрова", "maria@example.com", "+7 (999) 444-55-66", "password", "CLIENT");
        userService.save(client1);
        userService.save(client2);
    }

    private void createCars() {
        // Создаем автомобили
        Car car1 = new Car("Toyota Camry", "Черный", "TC123456", "Новый", new BigDecimal("3000"));
        Car car2 = new Car("Honda Accord", "Серебристый", "HA789012", "После ремонта", new BigDecimal("2500"));
        Car car3 = new Car("BMW X5", "Белый", "BW567890", "Новый", new BigDecimal("5000"));
        Car car4 = new Car("Mercedes E-Class", "Синий", "ME234567", "Новый", new BigDecimal("4500"));
        Car car5 = new Car("Volkswagen Passat", "Серый", "VP345678", "После ремонта", new BigDecimal("2000"));

        carService.save(car1);
        carService.save(car2);
        carService.save(car3);
        carService.save(car4);
        carService.save(car5);
    }
} 