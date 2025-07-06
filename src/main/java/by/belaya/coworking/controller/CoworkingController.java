package by.belaya.coworking.controller;

import by.belaya.coworking.model.*;
import by.belaya.coworking.repository.entity.Reservation;
import by.belaya.coworking.repository.entity.Workspace;
import by.belaya.coworking.service.api.IReservationService;
import by.belaya.coworking.service.api.IUserService;
import by.belaya.coworking.service.api.IWorkspaceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class CoworkingController {

    private final IUserService userService;
    private final IWorkspaceService workspaceService;
    private final IReservationService reservationService;

    private UserDTO currentUser;

    public CoworkingController(IUserService userService,
                               IWorkspaceService workspaceService,
                               IReservationService reservationService) {
        this.userService = userService;
        this.workspaceService = workspaceService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("login") String login, Model model) {
        try {
            var user = userService.login(new UserDTO(login.trim()));
            currentUser = new UserDTO(user.getLogin());

            if (user.getRole() == Role.ADMIN) {
                return "redirect:/admin/workspaces";
            } else {
                return "redirect:/customer/spaces";
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/admin/workspaces")
    public String listWorkspaces(Model model) {
        Set<Workspace> workspaces = workspaceService.findAll();
        model.addAttribute("workspaces", workspaces);
        return "admin/workspaces";
    }

    @GetMapping("/admin/workspaces/add")
    public String addWorkspaceForm(Model model) {
        model.addAttribute("workspaceDTO", new WorkspaceDTO());
        model.addAttribute("types", WorkspaceType.values());
        return "admin/addWorkspace";
    }

    @PostMapping("/admin/workspaces/add")
    public String addWorkspaceSubmit(
            @Valid @ModelAttribute("workspaceDTO") WorkspaceDTO workspaceDTO,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", WorkspaceType.values());
            return "admin/addWorkspace";
        }
        workspaceService.add(workspaceDTO);
        return "redirect:/admin/workspaces";
    }

    @GetMapping("/admin/workspaces/update/{id}")
    public String updateWorkspaceForm(@PathVariable("id") UUID id, Model model) {
        Workspace workspace = workspaceService.find(id);
        WorkspaceDTO dto = new WorkspaceDTO(workspace.getType(), workspace.getPrice(), workspace.isAvailable());
        model.addAttribute("workspaceDTO", dto);
        model.addAttribute("workspaceId", id);
        model.addAttribute("types", WorkspaceType.values());
        return "admin/updateWorkspace";
    }

    @PostMapping("/admin/workspaces/update/{id}")
    public String updateWorkspaceSubmit(
            @PathVariable("id") UUID id,
            @Valid @ModelAttribute("workspaceDTO") WorkspaceDTO workspaceDTO,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", WorkspaceType.values());
            model.addAttribute("workspaceId", id);
            return "admin/updateWorkspace";
        }
        workspaceService.update(id, workspaceDTO);
        return "redirect:/admin/workspaces";
    }

    @GetMapping("/admin/workspaces/delete/{id}")
    public String deleteWorkspace(@PathVariable("id") UUID id) {
        workspaceService.remove(id);
        return "redirect:/admin/workspaces";
    }

    @GetMapping("/admin/reservations")
    public String listReservations(Model model) {
        Set<Reservation> reservations = reservationService.findAll();
        model.addAttribute("reservations", reservations);
        return "admin/reservations";
    }

    @GetMapping("/customer/spaces")
    public String listAvailableSpaces(Model model) {
        Set<Workspace> available = workspaceService.findAvailable();
        model.addAttribute("availableWorkspaces", available);
        return "customer/availableSpaces";
    }

    @GetMapping("/customer/reservations/add")
    public String addReservationForm(Model model) {
        model.addAttribute("reservationDTO", new ReservationDTO());
        model.addAttribute("workspaces", workspaceService.findAvailable());
        System.out.println("Available workspaces: " + workspaceService.findAvailable());
        return "customer/addReservation";
    }

    @PostMapping("/customer/reservations/add")
    public String addReservationSubmit(
            @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workspaces", workspaceService.findAvailable());
            return "customer/addReservation";
        }
        reservationService.add(reservationDTO, currentUser);
        return "redirect:/customer/reservations";
    }

    @GetMapping("/customer/reservations")
    public String listMyReservations(Model model) {
        Set<Reservation> myReservations = reservationService.findByUser(currentUser);
        model.addAttribute("myReservations", myReservations);
        return "customer/myReservations";
    }

    @PostMapping("/customer/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable("id") UUID id) {
        reservationService.remove(id);
        return "redirect:/customer/reservations";
    }
}
