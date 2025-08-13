package org.example.staff_module.base.repository;

import org.example.staff_module.base.entity.AssignTask;
import org.example.staff_module.base.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignTaskRepo extends JpaRepository<Task,Integer> {
    @Query(value = "select * from assign_task where staff_id =:staff_id", nativeQuery = true)
    List<AssignTask> getAllAssignTasksByUserId(@Param("staff_id") Integer staff_id);
}
