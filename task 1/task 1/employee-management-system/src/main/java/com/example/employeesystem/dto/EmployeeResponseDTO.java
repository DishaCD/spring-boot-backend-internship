package com.example.employeesystem.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload returned to API clients.
 * Includes the generated employee ID; no validation annotations required.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Double salary;
    private String phoneNumber;
    private LocalDate joiningDate;
}
