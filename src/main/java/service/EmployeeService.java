package service;

import dto.EmployeeCreateRequest;
import dto.EmployeeResponse;
import dto.PageDto;
import exceptions.EmployeeException;

public interface EmployeeService {
    EmployeeResponse login(String email, String password) throws EmployeeException;
    EmployeeResponse createEmployee(EmployeeCreateRequest request) throws EmployeeException;
    PageDto<EmployeeResponse> getAllEmployeesPaginated(int page, int size) throws EmployeeException;
    EmployeeResponse getEmployeeById(Long id) throws EmployeeException;
    EmployeeResponse updateEmployee(Long id, EmployeeCreateRequest request) throws EmployeeException;
    void deleteEmployee(Long id) throws EmployeeException;
    void promoteEmployee(Long id) throws EmployeeException;
    void demoteEmployee(Long id) throws EmployeeException;
}