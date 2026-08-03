package com.enterprise.employeeapi.service;

import com.enterprise.employeeapi.dto.EmployeeRequest;
import com.enterprise.employeeapi.dto.EmployeeResponse;
import com.enterprise.employeeapi.entity.Department;
import com.enterprise.employeeapi.entity.Employee;
import com.enterprise.employeeapi.exception.DuplicateResourceException;
import com.enterprise.employeeapi.exception.ResourceNotFoundException;
import com.enterprise.employeeapi.mapper.EmployeeMapper;
import com.enterprise.employeeapi.repository.DepartmentRepository;
import com.enterprise.employeeapi.repository.EmployeeRepository;
import com.enterprise.employeeapi.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest employeeRequest;
    private Employee employee;
    private EmployeeResponse employeeResponse;
    private Department department;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("Engineering")
                .build();

        employeeRequest = EmployeeRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .jobTitle("Software Engineer")
                .salary(85000.0)
                .hireDate(LocalDate.of(2024, 1, 15))
                .departmentId(1L)
                .build();

        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .jobTitle("Software Engineer")
                .salary(85000.0)
                .hireDate(LocalDate.of(2024, 1, 15))
                .department(department)
                .build();

        employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .jobTitle("Software Engineer")
                .salary(85000.0)
                .hireDate(LocalDate.of(2024, 1, 15))
                .departmentName("Engineering")
                .departmentId(1L)
                .build();
    }

    @Test
    @DisplayName("Should create employee successfully")
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeMapper.toEntity(any(EmployeeRequest.class))).thenReturn(employee);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.createEmployee(employeeRequest);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void createEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(employeeRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("Should return all employees")
    void getAllEmployees_Success() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should return employee by ID")
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when employee not found")
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee");
    }

    @Test
    @DisplayName("Should update employee successfully")
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(any(Employee.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.updateEmployee(1L, employeeRequest);

        assertThat(result).isNotNull();
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent employee")
    void deleteEmployee_NotFound() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
