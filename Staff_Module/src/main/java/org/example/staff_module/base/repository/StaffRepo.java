package org.example.staff_module.base.repository;

import org.example.staff_module.base.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepo extends JpaRepository<Staff,Integer> {

    boolean existsByUsername(String username);

    Staff findByUsername(String username);
}
