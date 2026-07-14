package com.example.employeesystem.service;

import com.example.employeesystem.dto.EmployeeRequestDTO;
import com.example.employeesystem.dto.EmployeeResponseDTO;
import com.example.employeesystem.entity.Employee;
import com.example.employeesystem.exception.DuplicateEmailException;
import com.example.employeesystem.exception.ResourceNotFoundException;
import com.example.employeesystem.repository.EmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link EmployeeService}.
 * Encapsulates business rules such as unique email and not-found handling.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequest) {
        // Enforce unique email before persistence
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new DuplicateEmailException(
                    "Employee already exists with email: " + employeeRequest.getEmail());
        }

        Employee employee = modelMapper.map(employeeRequest, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeResponseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        return modelMapper.map(employee, EmployeeResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequest) {
        Employee existingEmployee = findEmployeeOrThrow(id);

        // Allow keeping the same email; reject only if another employee already uses it
        if (!existingEmployee.getEmail().equalsIgnoreCase(employeeRequest.getEmail())
                && employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new DuplicateEmailException(
                    "Employee already exists with email: " + employeeRequest.getEmail());
        }

        // Preserve existing ID while applying updated fields
        existingEmployee.setFirstName(employeeRequest.getFirstName());
        existingEmployee.setLastName(employeeRequest.getLastName());
        existingEmployee.setEmail(employeeRequest.getEmail());
        existingEmployee.setDepartment(employeeRequest.getDepartment());
        existingEmployee.setSalary(employeeRequest.getSalary());
        existingEmployee.setPhoneNumber(employeeRequest.getPhoneNumber());
        existingEmployee.setJoiningDate(employeeRequest.getJoiningDate());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return modelMapper.map(updatedEmployee, EmployeeResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    /**
     * Loads an employee by ID or throws {@link ResourceNotFoundException}.
     *
     * @param id employee identifier
     * @return managed employee entity
     */
    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id " + id));
    }
}
