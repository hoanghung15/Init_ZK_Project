package org.example.staff_module.base.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.repository.StaffRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController

@RequestMapping("staff")
public class StaffController {
    StaffRepo staffRepo;

    @PreAuthorize("hasRole('Nhân viên')")
    @GetMapping
    public List<Staff> getAll() {
        return staffRepo.findAll();
    }
}
