package org.example.staff_module.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.entity.Staff;
import org.example.staff_module.base.service.PartnerService;
import org.example.staff_module.dto.request.CreatePartnerRequest;
import org.example.staff_module.dto.response.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "partner-controller")
@RequestMapping("partner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PartnerController {
    PartnerService partnerService;

    @PostMapping("")
    public ApiResponse addNewPartner(@ParameterObject @Valid CreatePartnerRequest request) {
        return partnerService.createNewPartner(request);
    }
}
