package org.example.staff_module.base.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.config.custom.CustomUserDetail;
import org.example.staff_module.base.entity.AssignTask;
import org.example.staff_module.base.entity.Contract;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.repository.AssignTaskRepo;
import org.example.staff_module.base.repository.StaffRepo;
import org.example.staff_module.dto.response.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssignTaskService {
    AssignTaskRepo assignTaskRepo;
    StaffRepo staffRepo;

    public ApiResponse<List<AssignTask>> getAllAssignTasksByUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String username = userDetail.getUsername();
        Staff staff = staffRepo.findByUsername(username);

        return ApiResponse.<List<AssignTask>>builder()
                .code(200)
                .message("Get all assigned task successfully!")
                .result(assignTaskRepo.getAllAssignTasksByUserId(staff.getId()))
                .build();
    }
}
