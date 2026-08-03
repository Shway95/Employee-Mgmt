package com.enterprise.employeeapi.mapper;

import com.enterprise.employeeapi.dto.EmployeeRequest;
import com.enterprise.employeeapi.dto.EmployeeResponse;
import com.enterprise.employeeapi.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .jobTitle(request.getJobTitle())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .build();
    }

    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .jobTitle(employee.getJobTitle())
                .salary(employee.getSalary())
                .hireDate(employee.getHireDate())
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
