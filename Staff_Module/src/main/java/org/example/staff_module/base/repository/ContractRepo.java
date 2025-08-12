package org.example.staff_module.base.repository;

import org.example.staff_module.base.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepo extends JpaRepository<Contract, Integer> {

    @Query(value = "SELECT * FROM contract WHERE staff_id = :staff_id", nativeQuery = true)
    List<Contract> getContractToAccept(@Param("staff_id") Integer staffId);

}
