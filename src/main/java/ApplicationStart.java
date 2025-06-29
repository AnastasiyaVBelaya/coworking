import config.AppConfig;
import controller.MenuController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.api.IReservationService;
import service.api.IUserService;
import service.api.IWorkspaceService;

import java.util.Scanner;

public class ApplicationStart {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Scanner scanner = new Scanner(System.in);

            IUserService userService = context.getBean(IUserService.class);
            IWorkspaceService workspaceService = context.getBean(IWorkspaceService.class);
            IReservationService reservationService = context.getBean(IReservationService.class);

            MenuController menuController = new MenuController(scanner, userService, workspaceService, reservationService);
            menuController.run();
        }
    }
}
