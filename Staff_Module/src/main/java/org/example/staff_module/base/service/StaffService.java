package org.example.staff_module.base.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.entity.Contract;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.repository.ContractRepo;
import org.example.staff_module.base.repository.StaffRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class StaffService {
    JwtServiceImpl jwtService;
    StaffRepo staffRepo;
    ContractRepo contractRepo;
    public List<Contract> getListContractToAccept(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Staff staff = staffRepo.findByUsername(username);

        return contractRepo.getContractToAccept(staff.getId());


    }
}
