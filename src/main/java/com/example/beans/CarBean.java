package com.example.beans;

import com.example.model.Car;
import com.example.service.CarService;

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
public class CarBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private CarService carService;

    private Car car;
    private List<Car> cars;
    private List<Car> availableCars;
    private Long selectedCarId;

    @PostConstruct
    public void init() {
        car = new Car();
        loadCars();
    }

    public void init(javax.faces.event.ActionEvent event) {
        car = new Car();
        loadCars();
    }

    public void loadCars() {
        cars = carService.findAll();
        availableCars = carService.findAvailable();
    }

    public String createCar(javax.faces.event.ActionEvent event) {
        try {
            // Проверяем, не существует ли уже автомобиль с таким серийным номером
            if (carService.findBySerialNumber(car.getSerialNumber()) != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", 
                                "Автомобиль с таким серийным номером уже существует"));
                return null;
            }
            
            carService.save(car);
            car = new Car(); // сбрасываем форму
            loadCars();
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Автомобиль успешно добавлен"));
            
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось добавить автомобиль"));
            return null;
        }
    }

    public String updateCar(javax.faces.event.ActionEvent event) {
        try {
            carService.update(car);
            car = new Car(); // сбрасываем форму
            loadCars();
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Автомобиль успешно обновлен"));
            
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось обновить автомобиль"));
            return null;
        }
    }

    public void deleteCar(Car car) {
        try {
            carService.delete(car);
            loadCars();
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Автомобиль успешно удален"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось удалить автомобиль"));
        }
    }

    public void prepareEditCar(Car car) {
        this.car = car;
    }

    // Геттеры и сеттеры
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getAvailableCars() {
        return availableCars;
    }

    public void setAvailableCars(List<Car> availableCars) {
        this.availableCars = availableCars;
    }

    public Long getSelectedCarId() {
        return selectedCarId;
    }

    public void setSelectedCarId(Long selectedCarId) {
        this.selectedCarId = selectedCarId;
    }

    public Car getSelectedCar() {
        if (selectedCarId != null) {
            return carService.findById(selectedCarId);
        }
        return null;
    }
} 