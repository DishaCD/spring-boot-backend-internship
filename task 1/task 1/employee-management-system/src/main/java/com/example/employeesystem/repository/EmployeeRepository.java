package com.example.employeesystem.repository;

import com.example.employeesystem.entity.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Employee} persistence operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Checks whether an employee with the given email already exists.
     *
     * @param email employee email address
     * @return true if an employee with this email exists
     */
    boolean existsByEmail(String email);

    /**
     * Finds an employee by email address.
     *
     * @param email employee email address
     * @return optional employee if found
     */
    Optional<Employee> findByEmail(String email);
}
