package dto;

import model.Role;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String department,
        String jobPosition,
        BigDecimal salary,
        LocalDate hireDate,
        Boolean isActive,
        Role role,
        LocalDateTime createdAt
) {}