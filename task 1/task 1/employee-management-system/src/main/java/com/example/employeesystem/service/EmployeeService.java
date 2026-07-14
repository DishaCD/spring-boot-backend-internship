package com.example.employeesystem.service;

import com.example.employeesystem.dto.EmployeeRequestDTO;
import com.example.employeesystem.dto.EmployeeResponseDTO;
import java.util.List;

/**
 * Service contract for employee business operations.
 */
public interface EmployeeService {

    /**
     * Creates a new employee after validating email uniqueness.
     *
     * @param employee request data
     * @return created employee response
     */
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employee);

    /**
     * Retrieves all employees.
     *
     * @return list of employee responses
     */
    List<EmployeeResponseDTO> getAllEmployees();

    /**
     * Retrieves a single employee by ID.
     *
     * @param id employee identifier
     * @return employee response
     */
    EmployeeResponseDTO getEmployeeById(Long id);

    /**
     * Updates an existing employee while preserving the original ID.
     *
     * @param id       employee identifier
     * @param employee updated request data
     * @return updated employee response
     */
    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employee);

    /**
     * Deletes an employee by ID.
     *
     * @param id employee identifier
     */
    void deleteEmployee(Long id);
}
