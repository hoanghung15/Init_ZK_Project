package org.example.staff_module.base.repository;

import org.example.staff_module.base.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignTaskRepo extends JpaRepository<Task,Integer> {
}
