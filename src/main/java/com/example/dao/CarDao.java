package com.example.dao;

import com.example.model.Car;

import java.util.List;

public interface CarDao extends GenericDao<Car> {
    List<Car> findAvailable();
    Car findBySerialNumber(String serialNumber);
} 