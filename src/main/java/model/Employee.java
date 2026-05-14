package model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String department;
    private String jobPosition;
    private BigDecimal salary;
    private LocalDate hireDate;
    private Boolean isActive;
    private Role role;
    private LocalDateTime createdAt;
}