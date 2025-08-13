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
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "task")
public class Task {
    @Id
    int id;
    String type;

    String department;
    String description;

    @Column(name = "startDate")
    LocalDate startDate;
    @Column(name = "endDate")
    LocalDate endDate;

    @Column(name = "updatedAt")
    LocalDateTime updateAt;

    String status;

    @Column(name = "creatBy_id")
    Integer creatBy_id;


    @Column(name = "staff_id")
    Integer staff_id;

    @Column(name = "contract_id")
    Integer contract_id;
}
