package org.example.staff_module.base.repository;

import org.example.staff_module.base.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepo extends JpaRepository<Partner,Integer> {

    boolean findByMst(String mst);

    boolean existsByMst(String mst);
}
