package org.example.staff_module.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "contract")
public class Contract {
    @Id
    int id;
    @Column(name = "number_contract")
    String number_contract;
    String name;

    @Column(name = "email_a")
    String email_a;

    @Column(name = "email_b")
    String email_b;

    @Column(name = "phone_a")
    String phone_a;

    @Column(name = "phone_b")
    String phone_b;

    @Column(name = "staff_id")
    Integer staff_id;

    @Column(name = "file_data")
    String file_data;

    @Column(name = "employee_id")
    Integer employee_id;

    @Column(name = "contract_type")
    String contract_type;

    @Column(name = "contract_scope")
    String contract_scope;

    @Column(name = "start_date")
    LocalDate start_date;

    @Column(name = "end_date")
    LocalDate end_date;

    @Column(name = "payment_method")
    String payment_method;

    @Column(name = "status")
    String status;
}
