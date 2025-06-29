package controller;

import model.*;
import repository.entity.Reservation;
import repository.entity.User;
import repository.entity.Workspace;
import service.api.IReservationService;
import service.api.IUserService;
import service.api.IWorkspaceService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class MenuController {
    private final Scanner scanner;
    private final IUserService userService;
    private final IWorkspaceService workspaceService;
    private final IReservationService reservationService;
    private UserDTO currentUser;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    public MenuController(Scanner scanner,
                          IUserService userService,
                          IWorkspaceService workspaceService,
                          IReservationService reservationService) {
        this.scanner = scanner;
        this.userService = userService;
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to Coworking Management System");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            int choice = readIntInRange(3);
            System.out.println();
            switch (choice) {
                case 1, 2 -> login();
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }
        }
    }

    private void login() {
        System.out.print("Enter your login: ");
        System.out.println();
        String login = scanner.nextLine().trim();
        try {
            User user = userService.login(new UserDTO(login));
            System.out.println("Welcome, " + login);
            System.out.println();
            currentUser = new UserDTO(user.getLogin());
            if (user.getRole() == Role.ADMIN) {
                adminMenu();
            } else {
                customerMenu();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("Admin Menu");
            System.out.println("1. Add a new coworking space");
            System.out.println("2. Remove a coworking space");
            System.out.println("3. Update a coworking space");
            System.out.println("4. View all workspaces");
            System.out.println("5. View all reservations");
            System.out.println("6. Back to Main Menu");
            int choice = readIntInRange(6);
            System.out.println();
            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> updateWorkspace();
                case 4 -> viewAllWorkspaces();
                case 5 -> viewReservations();
                case 6 -> {
                    return;
                }
            }
        }
    }

    private void customerMenu() {
        while (true) {
            System.out.println("Customer Menu");
            System.out.println("1. Browse available spaces");
            System.out.println("2. Make a reservation");
            System.out.println("3. View my reservations");
            System.out.println("4. Cancel a reservation");
            System.out.println("5. Back to Main Menu");
            int choice = readIntInRange(5);
            System.out.println();
            switch (choice) {
                case 1 -> browseSpaces();
                case 2 -> makeReservation();
                case 3 -> viewMyReservations();
                case 4 -> cancelReservation();
                case 5 -> {
                    return;
                }
            }
        }
    }

    private void viewAllWorkspaces() {
        Set<Workspace> workspaces = workspaceService.findAll();
        if (workspaces.isEmpty()) {
            System.out.println("No workspaces available.");
            System.out.println();
        } else {
            System.out.println("All workspaces:");
            workspaces.stream()
                    .map(this::formatWorkspace)
                    .forEach(System.out::println);
            System.out.println();
        }
    }

    private void addWorkspace() {
        System.out.println("Enter workspace type (OPEN_SPACE, PRIVATE, ROOM): ");
        String typeInput = scanner.nextLine().toUpperCase();
        BigDecimal price = readPrice();
        try {
            workspaceService.add(new WorkspaceDTO(WorkspaceType.valueOf(typeInput), price, true));
            System.out.println("Workspace added successfully!");
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid workspace type!");
            System.out.println();
        }
    }

    private void removeWorkspace() {
        System.out.println("Enter workspace ID to remove: ");
        String idStr = scanner.nextLine();
        try {
            boolean removed = workspaceService.remove(UUID.fromString(idStr));
            if (removed) {
                System.out.println("Workspace removed successfully!");
            } else {
                System.out.println("Workspace not found!");
            }
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID format!");
            System.out.println();
        }
    }

    private void updateWorkspace() {
        System.out.println("Enter workspace ID to update: ");
        String idStr = scanner.nextLine();
        System.out.println("Enter new workspace type (OPEN_SPACE, PRIVATE, ROOM): ");
        String typeInput = scanner.nextLine().toUpperCase();
        BigDecimal newPrice = readPrice();
        try {
            Workspace updated = workspaceService.update(UUID.fromString(idStr),
                    new WorkspaceDTO(WorkspaceType.valueOf(typeInput), newPrice, true));
            System.out.println("Workspace updated successfully!");
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input! " + e.getMessage());
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    private void viewReservations() {
        Set<Reservation> reservations = reservationService.findAll();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            System.out.println();
        } else {
            reservations.forEach(System.out::println);
            System.out.println();
        }
    }

    private void browseSpaces() {
        Set<Workspace> available = workspaceService.findAvailable();
        if (available.isEmpty()) {
            System.out.println("No available workspaces.");
            System.out.println();
        } else {
            System.out.println("Available workspaces:");
            available.stream()
                    .map(this::formatWorkspace)
                    .forEach(System.out::println);
            System.out.println();
        }
    }

    private void makeReservation() {
        try {
            System.out.println("Enter workspace ID to reserve: ");
            System.out.println();
            UUID workspaceId = UUID.fromString(scanner.nextLine());
            System.out.println("Enter date for reservation (yyyy-mm-dd): ");
            System.out.println();
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.println("Enter start time (HH:mm): ");
            System.out.println();
            LocalTime startTime = LocalTime.parse(scanner.nextLine());
            System.out.println("Enter end time (HH:mm): ");
            System.out.println();
            LocalTime endTime = LocalTime.parse(scanner.nextLine());
            ReservationDTO reservationDTO =
                    new ReservationDTO(workspaceId, currentUser.getLogin(), date, startTime, endTime);
            reservationService.add(reservationDTO, currentUser);
            System.out.println("Reservation created successfully!");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println();
        }
    }

    private void viewMyReservations() {
        Set<Reservation> myReservations = reservationService.findByUser(currentUser);
        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            System.out.println();
        } else {
            myReservations.forEach(System.out::println);
            System.out.println();
        }
    }

    private void cancelReservation() {
        System.out.println("Enter reservation ID to cancel: ");
        System.out.println();
        try {
            UUID reservationId = UUID.fromString(scanner.nextLine());
            if (reservationService.remove(reservationId)) {
                System.out.println("Reservation canceled successfully.");
            } else {
                System.out.println("Reservation not found or access denied.");
            }
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid reservation ID format!");
            System.out.println();
        }
    }

    private int readIntInRange(int max) {
        String errorMsg = "Please enter a number between " + 1 + " and " + max;
        while (true) {
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < 1 || value > max) {
                    System.out.println(errorMsg);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
    }

    private BigDecimal readPrice() {
        while (true) {
            System.out.println("Enter workspace price: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Price cannot be empty!");
                continue;
            }
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format!");
            }
        }
    }

    private String formatPrice(BigDecimal price) {
        return PRICE_FORMAT.format(price);
    }

    private String formatWorkspace(Workspace workspace) {
        return String.format("Workspace{id=%s, type=%s, price=%s, available=%s}",
                workspace.getId(),
                workspace.getType(),
                formatPrice(workspace.getPrice()),
                workspace.isAvailable());
    }
}
