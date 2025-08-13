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

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "partner")
public class Partner {
    @Id
    int id;

    @Column(name = "partner_id")
    String partner_id;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @Column(name = "status")
    String status;

    @Column(name = "timestamp")
    Timestamp timestamp;

    @Column(name = "description")
    String description;
    
    @Column(name = "mst")
    String mst;
}
