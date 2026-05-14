package dto;

import model.Role;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        String department,
        String jobPosition,
        BigDecimal salary,
        LocalDate hireDate,
        Boolean isActive,
        Role role
) {}