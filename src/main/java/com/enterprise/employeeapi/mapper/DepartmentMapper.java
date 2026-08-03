package com.enterprise.employeeapi.mapper;

import com.enterprise.employeeapi.dto.DepartmentRequest;
import com.enterprise.employeeapi.dto.DepartmentResponse;
import com.enterprise.employeeapi.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest request) {
        return Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .employeeCount(department.getEmployees() != null ? department.getEmployees().size() : 0)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
