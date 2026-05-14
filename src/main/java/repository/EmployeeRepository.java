package repository;

import config.DatabaseConfig;
import model.Employee;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository {

    public Employee save(Employee employee) {
        String sql = """
                INSERT INTO employee 
                (first_name, last_name, email, password, phone_number, department, job_position, salary, hire_date, is_active, role) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPassword());
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setString(6, employee.getDepartment());
            stmt.setString(7, employee.getJobPosition());
            stmt.setBigDecimal(8, employee.getSalary());
            stmt.setDate(9, Date.valueOf(employee.getHireDate()));
            stmt.setBoolean(10, employee.getIsActive());
            stmt.setString(11, employee.getRole().name());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employee.setId(rs.getLong("id"));
                employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return employee;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving employee to database: " + e.getMessage());
        }
    }

    public List<Employee> findAllPaginated(int limit, int offset) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching paginated employees: " + e.getMessage());
        }
        return employees;
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM employee";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting employees: " + e.getMessage());
        }
        return 0;
    }

    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding employee: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding employee by email: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void update(Employee employee) {
        String sql = """
                UPDATE employee SET 
                first_name = ?, last_name = ?, email = ?, password = ?, phone_number = ?, 
                department = ?, job_position = ?, salary = ?, hire_date = ?, is_active = ?, role = ? 
                WHERE id = ?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPassword());
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setString(6, employee.getDepartment());
            stmt.setString(7, employee.getJobPosition());
            stmt.setBigDecimal(8, employee.getSalary());
            stmt.setDate(9, Date.valueOf(employee.getHireDate()));
            stmt.setBoolean(10, employee.getIsActive());
            stmt.setString(11, employee.getRole().name());
            stmt.setLong(12, employee.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating employee: " + e.getMessage());
        }
    }

    public void updateRole(Long id, Role role) {
        String sql = "UPDATE employee SET role = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating role: " + e.getMessage());
        }
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting employee: " + e.getMessage());
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return Employee.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .phoneNumber(rs.getString("phone_number"))
                .department(rs.getString("department"))
                .jobPosition(rs.getString("job_position"))
                .salary(rs.getBigDecimal("salary"))
                .hireDate(rs.getDate("hire_date").toLocalDate())
                .isActive(rs.getBoolean("is_active"))
                .role(Role.valueOf(rs.getString("role")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}