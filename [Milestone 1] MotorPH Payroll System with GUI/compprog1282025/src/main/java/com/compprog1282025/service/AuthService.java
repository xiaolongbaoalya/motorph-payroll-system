package com.compprog1282025.service;

public interface AuthService {
    boolean authenticateEmployee(int empNumber, String password);
    boolean authenticateAdmin(String username, String password);
}