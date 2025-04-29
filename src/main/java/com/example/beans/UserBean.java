package com.example.beans;

import com.example.model.User;
import com.example.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

@Named
@ViewScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s-()]{10,15}$");

    @Inject
    private UserService userService;

    @Inject
    private SessionBean sessionBean;

    private User user;
    private String confirmPassword;
    private String email;
    private String password;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String login() {
        try {
            User foundUser = userService.findByEmail(email);
            if (foundUser == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка входа", "Пользователь с email " + email + " не найден"));
                return null;
            }
            
            if (!foundUser.getPassword().equals(password)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка входа", "Неверный пароль"));
                return null;
            }
            
            sessionBean.setCurrentUser(foundUser);
            return foundUser.getRole().equals("MANAGER") ? "manager" : "client";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Системная ошибка", "Ошибка при входе: " + e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    public String register() {
        if (!validateRegistration()) {
            return null;
        }

        // Проверяем, не занят ли уже email
        if (userService.findByEmail(user.getEmail()) != null) {
            FacesContext.getCurrentInstance().addMessage("registerForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Пользователь с таким email уже существует"));
            return null;
        }

        user.setRole("CLIENT"); // По умолчанию регистрируем клиентов
        userService.save(user);
        
        // Автоматически входим после регистрации
        sessionBean.setCurrentUser(user);
        
        return "client";
    }

    public String logout() {
        // Сбрасываем текущего пользователя
        sessionBean.setCurrentUser(null);
        
        // Получаем текущую сессию и инвалидируем её
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        return "/login?faces-redirect=true";
    }

    public void logout(javax.faces.event.ActionEvent event) {
        // Сбрасываем текущего пользователя
        sessionBean.setCurrentUser(null);
        
        // Получаем текущую сессию и инвалидируем её
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "/login?faces-redirect=true");
    }

    private boolean validateRegistration() {
        boolean valid = true;
        FacesContext context = FacesContext.getCurrentInstance();

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            context.addMessage("registerForm:name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Имя не может быть пустым"));
            valid = false;
        }

        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            context.addMessage("registerForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Введите корректный email"));
            valid = false;
        }

        if (user.getPhone() != null && !user.getPhone().isEmpty() &&
                !PHONE_PATTERN.matcher(user.getPhone()).matches()) {
            context.addMessage("registerForm:phone",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Введите корректный номер телефона"));
            valid = false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            context.addMessage("registerForm:password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Пароль не может быть пустым"));
            valid = false;
        }

        if (confirmPassword == null || !confirmPassword.equals(user.getPassword())) {
            context.addMessage("registerForm:confirmPassword",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Пароли не совпадают"));
            valid = false;
        }

        return valid;
    }

    // Геттеры и сеттеры
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 