package com.example.beans;

import com.example.model.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isManager() {
        return isLoggedIn() && "MANAGER".equals(currentUser.getRole());
    }

    public boolean isClient() {
        return isLoggedIn() && "CLIENT".equals(currentUser.getRole());
    }
} 