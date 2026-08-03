package com.enterprise.employeeapi.service;

import com.enterprise.employeeapi.dto.DepartmentRequest;
import com.enterprise.employeeapi.dto.DepartmentResponse;
import com.enterprise.employeeapi.entity.Department;
import com.enterprise.employeeapi.exception.DuplicateResourceException;
import com.enterprise.employeeapi.exception.ResourceNotFoundException;
import com.enterprise.employeeapi.mapper.DepartmentMapper;
import com.enterprise.employeeapi.repository.DepartmentRepository;
import com.enterprise.employeeapi.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentRequest departmentRequest;
    private Department department;
    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        departmentRequest = DepartmentRequest.builder()
                .name("Engineering")
                .description("Software Engineering Department")
                .build();

        department = Department.builder()
                .id(1L)
                .name("Engineering")
                .description("Software Engineering Department")
                .build();

        departmentResponse = DepartmentResponse.builder()
                .id(1L)
                .name("Engineering")
                .description("Software Engineering Department")
                .employeeCount(0)
                .build();
    }

    @Test
    @DisplayName("Should create department successfully")
    void createDepartment_Success() {
        when(departmentRepository.existsByName(anyString())).thenReturn(false);
        when(departmentMapper.toEntity(any(DepartmentRequest.class))).thenReturn(department);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.createDepartment(departmentRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Engineering");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("Should throw exception when department name already exists")
    void createDepartment_DuplicateName() {
        when(departmentRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> departmentService.createDepartment(departmentRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("name");
    }

    @Test
    @DisplayName("Should return all departments")
    void getAllDepartments_Success() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        List<DepartmentResponse> result = departmentService.getAllDepartments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Engineering");
    }

    @Test
    @DisplayName("Should return department by ID")
    void getDepartmentById_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(any(Department.class))).thenReturn(departmentResponse);

        DepartmentResponse result = departmentService.getDepartmentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when department not found")
    void getDepartmentById_NotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getDepartmentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("Should delete department successfully")
    void deleteDepartment_Success() {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent department")
    void deleteDepartment_NotFound() {
        when(departmentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> departmentService.deleteDepartment(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
