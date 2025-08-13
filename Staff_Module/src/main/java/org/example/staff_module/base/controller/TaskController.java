package org.example.staff_module.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.staff_module.base.entity.AssignTask;
import org.example.staff_module.base.service.AssignTaskService;
import org.example.staff_module.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "task-controller")
@RequestMapping("task")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
AssignTaskService assignTaskService;
    @GetMapping
    public ApiResponse<List<AssignTask>> findAllAssignTask(){
        return assignTaskService.getAllAssignTasksByUserId();
    }
}
