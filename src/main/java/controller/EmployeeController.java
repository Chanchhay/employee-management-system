package controller;

import dto.EmployeeCreateRequest;
import dto.EmployeeResponse;
import dto.PageDto;
import model.Role;
import service.EmployeeService;
import view.EmployeeView;

public class EmployeeController {

    private final EmployeeView view;
    private final EmployeeService service;
    private EmployeeResponse currentUser;

    public EmployeeController(EmployeeView view, EmployeeService service) {
        this.view = view;
        this.service = service;
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                handleLogin();
            } else if (currentUser.role() == Role.ADMIN) {
                handleAdminFlow();
            } else {
                handleStaffFlow();
            }
        }
    }

    private void handleLogin() {
        String[] credentials = view.showLoginForm();
        try {
            currentUser = service.login(credentials[0], credentials[1]);
            view.displayMessage("Welcome back, " + currentUser.firstName() + "!");
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void handleAdminFlow() {
        int opt = view.showAdminMenu();
        switch (opt) {
            case 1 -> create();
            case 2 -> update();
            case 3 -> getAllPaginated();
            case 4 -> getById();
            case 5 -> changeRole(true);
            case 6 -> changeRole(false);
            case 7 -> delete();
            case 8 -> logout();
            case 0 -> exitSystem();
            default -> view.displayError("Invalid option.");
        }
    }

    private void handleStaffFlow() {
        int opt = view.showStaffMenu();
        switch (opt) {
            case 1 -> view.displayEmployeeResponse(currentUser, "My Profile");
            case 2 -> getAllPaginated();
            case 3 -> logout();
            case 0 -> exitSystem();
            default -> view.displayError("Invalid option.");
        }
    }

    private void create() {
        try {
            EmployeeCreateRequest request = view.createEmployeeForm(true);
            EmployeeResponse response = service.createEmployee(request);
            view.displayEmployeeResponse(response, "Employee Created");
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void update() {
        try {
            Long id = view.getLongInput("\n[+] Enter ID to update: ");
            EmployeeResponse existing = service.getEmployeeById(id);
            view.displayEmployeeResponse(existing, "Current Data");
            EmployeeCreateRequest request = view.createEmployeeForm(true);
            EmployeeResponse updated = service.updateEmployee(id, request);
            view.displayEmployeeResponse(updated, "Employee Updated");
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void getAllPaginated() {
        int page = 1;
        int size = 5;
        while (true) {
            try {
                PageDto<EmployeeResponse> pageDto = service.getAllEmployeesPaginated(page, size);
                view.displayTableEmployeePaginated(pageDto);

                if (pageDto.totalPages() <= 1) break;

                String cmd = view.getPaginationCommand();
                if (cmd.equals("N") && page < pageDto.totalPages()) page++;
                else if (cmd.equals("P") && page > 1) page--;
                else if (cmd.equals("E")) break;
            } catch (Exception e) {
                view.displayError(e.getMessage());
                break;
            }
        }
    }

    private void getById() {
        try {
            Long id = view.getLongInput("\n[+] Enter ID: ");
            EmployeeResponse response = service.getEmployeeById(id);
            view.displayEmployeeResponse(response, "Employee Found");
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void changeRole(boolean promote) {
        try {
            Long id = view.getLongInput("\n[+] Enter ID: ");
            if (promote) {
                service.promoteEmployee(id);
                view.displayMessage("Employee promoted to ADMIN.");
            } else {
                service.demoteEmployee(id);
                view.displayMessage("Employee demoted to STAFF.");
            }
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void delete() {
        try {
            Long id = view.getLongInput("\n[+] Enter ID to delete: ");
            EmployeeResponse existing = service.getEmployeeById(id);
            view.displayEmployeeResponse(existing, "Selected for Deletion");
            if (view.getConfirmation("Delete this employee?")) {
                service.deleteEmployee(id);
                view.displayMessage("Employee deleted.");
            }
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        view.displayMessage("Logged out successfully.");
    }

    private void exitSystem() {
        view.displayMessage("Goodbye!");
        System.exit(0);
    }
}