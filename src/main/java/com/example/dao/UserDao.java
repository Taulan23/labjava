package com.example.dao;

import com.example.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User> {
    User findByEmail(String email);
    List<User> findByRole(String role);
} 