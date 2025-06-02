package com.compprog1282025.controller;

import com.compprog1282025.service.AuthService;

public class MainController {
    private AuthService authService;

    public MainController(AuthService authService) {
        this.authService = authService;
    }

    public void handleLogin(String username, String password) {
        if (authService.authenticateAdmin(username, password)) {
            // Show admin panel
        } else if (authService.authenticateEmployee(Integer.parseInt(username), password)) {
            // Show employee dashboard
        } else {
            // Show error
        }
    }
}