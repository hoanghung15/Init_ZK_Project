package org.example.staff_module.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.Enum.PartnerStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePartnerRequest {
    @NotNull
    String partner_id;

    @NotNull
    String mst;

    @NotNull
    String name;

    @NotNull
    String address;

    PartnerStatus status;


    String description;
}
