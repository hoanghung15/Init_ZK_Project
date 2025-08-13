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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "assign_task")
public class AssignTask {
    @Id
    int id;

    @Column(name = "staff_id")
    Integer staff_id;

    @Column(name = "task_id")
    Integer task_id;

    @Column(name = "user_id")
    Integer user_id;

    @Column(name = "assign_date")
    LocalDate assign_date;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    String status;
}
