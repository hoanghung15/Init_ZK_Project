package org.example.staff_module.base.service.inf;

import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.dto.response.ApiResponse;
import org.example.staff_module.dto.response.AuthResponse;

public interface StaffService {
    Staff findByUsername(String username);
    ApiResponse<AuthResponse> login(String username, String password);
}