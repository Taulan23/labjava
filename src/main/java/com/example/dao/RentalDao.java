package com.example.dao;

import com.example.model.Rental;
import com.example.model.User;

import java.util.List;

public interface RentalDao extends GenericDao<Rental> {
    List<Rental> findActiveRentals();
    List<Rental> findActiveRentalsByClient(User client);
} 