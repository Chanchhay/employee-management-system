package mapper;

import dto.EmployeeCreateRequest;
import dto.EmployeeResponse;
import model.Employee;

public class EmployeeMapper {

    public Employee fromEmployeeCreateRequest(EmployeeCreateRequest request) {
        return Employee.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .phoneNumber(request.phoneNumber())
                .department(request.department())
                .jobPosition(request.jobPosition())
                .salary(request.salary())
                .hireDate(request.hireDate())
                .isActive(request.isActive() != null ? request.isActive() : true)
                .role(request.role() != null ? request.role() : model.Role.STAFF)
                .build();
    }

    public EmployeeResponse toEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getDepartment(),
                employee.getJobPosition(),
                employee.getSalary(),
                employee.getHireDate(),
                employee.getIsActive(),
                employee.getRole(),
                employee.getCreatedAt()
        );
    }
}