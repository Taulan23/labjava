package com.example.service;

import com.example.dao.CarDao;
import com.example.model.Car;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CarService {

    @Inject
    private CarDao carDao;

    public Car findById(Long id) {
        return carDao.findById(id);
    }

    public List<Car> findAll() {
        return carDao.findAll();
    }

    public List<Car> findAvailable() {
        return carDao.findAvailable();
    }

    public Car findBySerialNumber(String serialNumber) {
        return carDao.findBySerialNumber(serialNumber);
    }

    public void save(Car car) {
        carDao.save(car);
    }

    public void update(Car car) {
        carDao.update(car);
    }

    public void delete(Car car) {
        carDao.delete(car);
    }

    public void deleteById(Long id) {
        carDao.deleteById(id);
    }
} 