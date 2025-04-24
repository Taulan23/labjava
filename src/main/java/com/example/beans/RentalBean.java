package com.example.beans;

import com.example.model.Car;
import com.example.model.Rental;
import com.example.model.User;
import com.example.service.CarService;
import com.example.service.RentalService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named
@ViewScoped
public class RentalBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private RentalService rentalService;

    @Inject
    private CarService carService;

    @Inject
    private SessionBean sessionBean;

    private List<Rental> activeRentals;
    private List<Rental> userRentals;
    private Car selectedCar;
    private int rentalDays = 1;
    private BigDecimal totalPrice;

    @PostConstruct
    public void init() {
        loadRentals();
    }

    public void loadRentals() {
        activeRentals = rentalService.findActiveRentals();
        if (sessionBean.isClient()) {
            userRentals = rentalService.findActiveRentalsByClient(sessionBean.getCurrentUser());
        }
    }

    public void selectCar(Car car) {
        this.selectedCar = car;
        this.rentalDays = 1;
        calculateTotalPrice();
    }

    public void selectCar(Car car, javax.faces.event.ActionEvent event) {
        this.selectedCar = car;
        this.rentalDays = 1;
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        if (selectedCar != null && rentalDays > 0) {
            totalPrice = selectedCar.getDailyPrice().multiply(BigDecimal.valueOf(rentalDays));
        } else {
            totalPrice = BigDecimal.ZERO;
        }
    }

    public String rentCar() {
        if (selectedCar == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Автомобиль не выбран"));
            return null;
        }

        if (rentalDays <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Количество дней должно быть больше 0"));
            return null;
        }

        try {
            User client = sessionBean.getCurrentUser();
            rentalService.createRental(selectedCar, client, rentalDays);
            
            // Очищаем форму
            selectedCar = null;
            rentalDays = 1;
            totalPrice = null;
            
            // Обновляем данные
            loadRentals();
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Автомобиль успешно арендован"));
            
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось арендовать автомобиль"));
            return null;
        }
    }

    public String rentCar(javax.faces.event.ActionEvent event) {
        return rentCar();
    }

    public void returnCar(Rental rental) {
        try {
            rentalService.returnCar(rental);
            loadRentals();
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Автомобиль успешно возвращен"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось вернуть автомобиль"));
        }
    }

    public void returnCar(Rental rental, javax.faces.event.ActionEvent event) {
        returnCar(rental);
    }

    // Геттеры и сеттеры
    public List<Rental> getActiveRentals() {
        return activeRentals;
    }

    public void setActiveRentals(List<Rental> activeRentals) {
        this.activeRentals = activeRentals;
    }

    public List<Rental> getUserRentals() {
        return userRentals;
    }

    public void setUserRentals(List<Rental> userRentals) {
        this.userRentals = userRentals;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String cancelSelection() {
        this.selectedCar = null;
        this.rentalDays = 1;
        this.totalPrice = null;
        return null;
    }

    public String cancelSelection(javax.faces.event.ActionEvent event) {
        return cancelSelection();
    }
} 