package com.compprog1282025.util;

import com.compprog1282025.service.AuthService;
import com.compprog1282025.model.Employee;
import com.compprog1282025.model.Admin;

public class LoginManager implements AuthService {
    @Override
    public boolean authenticateEmployee(int empNumber, String password) {
        // lookup employee and check password
        return true;
    }

    @Override
    public boolean authenticateAdmin(String username, String password) {
        // lookup admin and check username + password
        return true;
    }
}