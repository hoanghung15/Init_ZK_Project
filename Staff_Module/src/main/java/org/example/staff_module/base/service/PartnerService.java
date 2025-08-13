package org.example.staff_module.base.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.entity.Partner;
import org.example.staff_module.base.repository.PartnerRepo;
import org.example.staff_module.dto.request.CreatePartnerRequest;
import org.example.staff_module.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PartnerService {
    PartnerRepo partnerRepo;

    public ApiResponse createNewPartner(CreatePartnerRequest request) {

        boolean check = partnerRepo.existsByMst((request.getMst()));
        if (check) {
            throw new RuntimeException("Đã tồn tại đối tác với mã số thuế");
        } else {

            Partner partner = new Partner();
            partner.setPartner_id(request.getPartner_id());
            partner.setName(request.getName());
            partner.setAddress(request.getAddress());
            partner.setStatus(request.getStatus().getDescription());
            partner.setTimestamp(new Timestamp(System.currentTimeMillis()));
            partner.setDescription(request.getDescription());
            partner.setMst(request.getMst());

            partnerRepo.save(partner);
            return ApiResponse.builder()
                    .code(200)
                    .message("Created partner successfully")
                    .build();
        }
    }

}
