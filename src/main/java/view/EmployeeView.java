package view;

import dto.EmployeeCreateRequest;
import dto.EmployeeResponse;
import dto.PageDto;
import model.Role;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EmployeeView {

    private final Scanner scanner = new Scanner(System.in);

    public String[] showLoginForm() {
        System.out.println("\n=== [[ System Login ]] ===");
        System.out.print("[+] Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("[+] Password: ");
        String password = scanner.nextLine().trim();
        return new String[]{email, password};
    }

    public int showAdminMenu() {
        System.out.println("""
                
                ========================================
                [+] ADMIN DASHBOARD
                ========================================
                1. Create New Employee
                2. Update Existing Employee
                3. View All Employees (Paginated)
                4. Find Employee By ID
                5. Promote Employee to ADMIN
                6. Demote Employee to STAFF
                7. Delete Employee
                8. Logout
                0. Exit System
                ========================================
                """);
        System.out.print("Choose an option: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public int showStaffMenu() {
        System.out.println("""
                
                ========================================
                [+] STAFF DASHBOARD
                ========================================
                1. View My Profile
                2. View All Employees Directory
                3. Logout
                0. Exit System
                ========================================
                """);
        System.out.print("Choose an option: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public EmployeeCreateRequest createEmployeeForm(boolean isAdmin) {
        System.out.println("\n=== [[ Employee Details Form ]] ===");

        String firstName = getValidatedString("[+] First Name: ", "^[A-Za-z]{2,}$", "Invalid name.");
        String lastName = getValidatedString("[+] Last Name: ", "^[A-Za-z]{2,}$", "Invalid name.");
        String email = getValidatedString("[+] Email: ", "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", "Invalid email.");
        String password = getValidatedString("[+] Password (min 6 chars): ", "^.{6,}$", "Password too short.");
        String phone = getValidatedString("[+] Phone Number: ", "^\\+?[0-9\\-\\s]{7,15}$", "Invalid phone.");
        String department = getValidatedString("[+] Department: ", "^.+$", "Cannot be empty.");
        String position = getValidatedString("[+] Job Position: ", "^.+$", "Cannot be empty.");
        BigDecimal salary = getBigDecimalInput("[+] Salary (e.g., 50000.00): ");
        LocalDate hire = getLocalDateInput("[+] Hire Date (yyyy-MM-dd): ");
        Boolean isActive = getBooleanInput("[+] Is Active Employee? (Y/N): ");

        Role role = Role.STAFF;
        if (isAdmin) {
            String roleInput = getValidatedString("[+] Role (ADMIN/STAFF): ", "^(?i)(ADMIN|STAFF)$", "Must be ADMIN or STAFF.");
            role = Role.valueOf(roleInput.toUpperCase());
        }

        return new EmployeeCreateRequest(
                firstName, lastName, email, password, phone, department, position, salary, hire, isActive, role
        );
    }

    public void displayEmployeeResponse(EmployeeResponse response, String context) {
        System.out.println("\n[*] " + context);
        Table table = new Table(2, BorderStyle.UNICODE_ROUND_BOX);
        table.addCell(" Attribute ", 1); table.addCell(" Value ", 1);
        table.addCell(" ID"); table.addCell(" " + response.id());
        table.addCell(" Full Name"); table.addCell(" " + response.firstName() + " " + response.lastName());
        table.addCell(" Role"); table.addCell(" " + response.role());
        table.addCell(" Email"); table.addCell(" " + response.email());
        table.addCell(" Phone"); table.addCell(" " + response.phoneNumber());
        table.addCell(" Department"); table.addCell(" " + response.department());
        table.addCell(" Job Position"); table.addCell(" " + response.jobPosition());
        table.addCell(" Salary"); table.addCell(" $" + response.salary());
        table.addCell(" Hire Date"); table.addCell(" " + response.hireDate());
        table.addCell(" Status"); table.addCell(response.isActive() ? " Active" : " Inactive");
        System.out.println(table.render());
    }

    public void displayTableEmployeePaginated(PageDto<EmployeeResponse> pageDto) {
        if (pageDto.content().isEmpty()) {
            displayMessage("No employees found.");
            return;
        }

        Table table = new Table(8, BorderStyle.UNICODE_ROUND_BOX);
        String[] columns = {"ID", "Full Name", "Role", "Email", "Department", "Position", "Salary", "Status"};
        for (String column : columns) table.addCell(" " + column + " ");

        for (EmployeeResponse user : pageDto.content()) {
            table.addCell(" " + user.id().toString());
            table.addCell(" " + user.firstName() + " " + user.lastName());
            table.addCell(" " + user.role());
            table.addCell(" " + user.email());
            table.addCell(" " + user.department());
            table.addCell(" " + user.jobPosition());
            table.addCell(" $" + user.salary().toString());
            table.addCell(user.isActive() ? " Active " : " Inactive ");
        }

        System.out.println(table.render());
        System.out.println("Page " + pageDto.currentPage() + " of " + pageDto.totalPages() + " | Total Records: " + pageDto.totalElements());
    }

    public String getPaginationCommand() {
        System.out.print("[N]ext Page | [P]revious Page | [E]xit to Menu: ");
        return scanner.nextLine().trim().toUpperCase();
    }

    public void displayMessage(String message) { System.out.println("\n[*] " + message); }
    public void displayError(String error) { System.out.println("\n[!] ERROR: " + error); }
    public boolean getConfirmation(String prompt) { return getBooleanInput(prompt + " (Y/N): "); }

    public Long getLongInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Long.parseLong(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("[!] Invalid ID."); }
        }
    }
    private String getValidatedString(String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(regex)) return input;
            System.out.println("[!] " + errorMsg);
        }
    }
    private BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return new BigDecimal(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("[!] Invalid value."); }
        }
    }
    private LocalDate getLocalDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return LocalDate.parse(scanner.nextLine().trim()); }
            catch (DateTimeParseException e) { System.out.println("[!] Format: yyyy-MM-dd."); }
        }
    }
    private Boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("[!] Enter Y or N.");
        }
    }
}