package org.example.librex.database.notification;

import org.example.librex.database.notification.dto.NotificationResponse;
import org.example.librex.database.users.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AppUserService appUserService;

    public NotificationController(NotificationService notificationService, AppUserService appUserService) {
        this.notificationService = notificationService;
        this.appUserService = appUserService;
    }

    private Integer getCurrentUserId(Authentication auth) {
        return appUserService.findByUsername(auth.getName()).getId();
    }

    @GetMapping
    public List<NotificationResponse> getUnreadNotifications(Authentication auth) {
        Integer userId = getCurrentUserId(auth);
        return notificationService.getUserUnreadNotifications(userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
    }
}

