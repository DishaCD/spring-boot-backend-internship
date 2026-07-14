package com.example.employeesystem.config;

import com.example.employeesystem.entity.Employee;
import com.example.employeesystem.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Seeds the database with default employee records on startup.
 */
@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Employee> sampleEmployees = List.of(
                    Employee.builder()
                        .firstName("Hamshika")
                        .lastName("A")
                        .email("hamshika.a@example.com")
                        .department("Engineering")
                        .salary(95000.0)
                        .phoneNumber("9876543210")
                        .joiningDate(LocalDate.of(2026, 1, 15))
                        .build(),
                    Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .department("Marketing")
                        .salary(60000.0)
                        .phoneNumber("9876543211")
                        .joiningDate(LocalDate.of(2026, 2, 20))
                        .build(),
                    Employee.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .department("Human Resources")
                        .salary(65000.0)
                        .phoneNumber("9876543212")
                        .joiningDate(LocalDate.of(2026, 3, 10))
                        .build(),
                    Employee.builder()
                        .firstName("Robert")
                        .lastName("Johnson")
                        .email("robert.johnson@example.com")
                        .department("Finance")
                        .salary(85000.0)
                        .phoneNumber("9876543213")
                        .joiningDate(LocalDate.of(2025, 11, 1))
                        .build(),
                    Employee.builder()
                        .firstName("Emily")
                        .lastName("Davis")
                        .email("emily.davis@example.com")
                        .department("Product Management")
                        .salary(90000.0)
                        .phoneNumber("9876543214")
                        .joiningDate(LocalDate.of(2026, 5, 18))
                        .build()
                );
                
                repository.saveAll(sampleEmployees);
                System.out.println("Database successfully seeded with 5 sample employee records.");
            }
        };
    }
}
