package service.impl;

import dto.EmployeeCreateRequest;
import dto.EmployeeResponse;
import dto.PageDto;
import exceptions.EmployeeException;
import mapper.EmployeeMapper;
import model.Employee;
import model.Role;
import repository.EmployeeRepository;
import service.EmployeeService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EmployeeResponse login(String email, String password) throws EmployeeException {
        Employee employee = repository.findByEmail(email)
                .orElseThrow(() -> new EmployeeException("Invalid email or password."));
        if (!employee.getPassword().equals(password)) {
            throw new EmployeeException("Invalid email or password.");
        }
        if (!employee.getIsActive()) {
            throw new EmployeeException("Account is deactivated.");
        }
        return mapper.toEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) throws EmployeeException {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new EmployeeException("Email already exists.");
        }
        Employee employee = mapper.fromEmployeeCreateRequest(request);
        repository.save(employee);
        return mapper.toEmployeeResponse(employee);
    }

    @Override
    public PageDto<EmployeeResponse> getAllEmployeesPaginated(int page, int size) throws EmployeeException {
        int offset = (page - 1) * size;
        List<EmployeeResponse> content = repository.findAllPaginated(size, offset)
                .stream()
                .map(mapper::toEmployeeResponse)
                .toList();

        long totalElements = repository.countAll();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageDto<>(content, page, totalPages, totalElements);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) throws EmployeeException {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee with ID " + id + " not found."));
        return mapper.toEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeCreateRequest request) throws EmployeeException {
        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee with ID " + id + " not found."));

        existingEmployee.setFirstName(request.firstName());
        existingEmployee.setLastName(request.lastName());
        existingEmployee.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            existingEmployee.setPassword(request.password());
        }
        existingEmployee.setPhoneNumber(request.phoneNumber());
        existingEmployee.setDepartment(request.department());
        existingEmployee.setJobPosition(request.jobPosition());
        existingEmployee.setSalary(request.salary());
        existingEmployee.setHireDate(request.hireDate());
        existingEmployee.setIsActive(request.isActive());
        existingEmployee.setRole(request.role());

        repository.update(existingEmployee);
        return mapper.toEmployeeResponse(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) throws EmployeeException {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new EmployeeException("Failed to delete. Employee with ID " + id + " not found.");
        }
    }

    @Override
    public void promoteEmployee(Long id) throws EmployeeException {
        if (repository.findById(id).isEmpty()) throw new EmployeeException("Employee not found.");
        repository.updateRole(id, Role.ADMIN);
    }

    @Override
    public void demoteEmployee(Long id) throws EmployeeException {
        if (repository.findById(id).isEmpty()) throw new EmployeeException("Employee not found.");
        repository.updateRole(id, Role.STAFF);
    }
}