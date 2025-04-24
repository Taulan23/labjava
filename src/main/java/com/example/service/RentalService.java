package com.example.service;

import com.example.dao.RentalDao;
import com.example.model.Car;
import com.example.model.Rental;
import com.example.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class RentalService {

    @Inject
    private RentalDao rentalDao;

    @Inject
    private CarService carService;

    public Rental findById(Long id) {
        return rentalDao.findById(id);
    }

    public List<Rental> findAll() {
        return rentalDao.findAll();
    }

    public List<Rental> findActiveRentals() {
        return rentalDao.findActiveRentals();
    }

    public List<Rental> findActiveRentalsByClient(User client) {
        return rentalDao.findActiveRentalsByClient(client);
    }

    public void save(Rental rental) {
        rentalDao.save(rental);
    }

    public void update(Rental rental) {
        rentalDao.update(rental);
    }

    public void delete(Rental rental) {
        rentalDao.delete(rental);
    }

    public void deleteById(Long id) {
        rentalDao.deleteById(id);
    }

    public Rental createRental(Car car, User client, int days) {
        // Создаем новую аренду
        Rental rental = new Rental(car, client, LocalDate.now(), days);
        
        // Обновляем доступность автомобиля
        car.setAvailable(false);
        carService.update(car);
        
        // Сохраняем аренду
        save(rental);
        
        return rental;
    }

    public void returnCar(Rental rental) {
        // Помечаем аренду как неактивную
        rental.setActive(false);
        update(rental);
        
        // Делаем автомобиль снова доступным
        Car car = rental.getCar();
        car.setAvailable(true);
        carService.update(car);
    }
} 