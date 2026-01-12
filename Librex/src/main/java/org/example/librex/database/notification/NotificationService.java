package org.example.librex.database.notification;

import org.example.librex.database.notification.dto.NotificationResponse;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.database.users.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final AppUserRepository appUserRepository;

    public NotificationService(NotificationRepository repository, AppUserRepository userRepository) {
        this.repository = repository;
        this.appUserRepository = userRepository;
    }

    //Creates custom titled notification
    @Transactional
    public void createNotification(
            Integer userId,
            String title,
            String content
    ) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Notification notification = new Notification(
                user,
                title,
                content
        );

        repository.save(notification);
    }

    @Transactional
    public void createBookAvailableNotification(
            Integer userId,
            String bookTitle
    ) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Notification notification = new Notification(
                user,
                "Książka na którą czekasz jest dostępna!",
                bookTitle
        );

        repository.save(notification);
    }

    @Transactional
    public void markAsRead(Integer notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow();

        notification.markAsRead();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Integer userId) {
        List<Notification> Notifications = repository.findByUser_IdOrderByCreatedAtDesc(userId);

        return Notifications
                .stream()
                .map(NotificationResponse::from)
                .toList();

    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserUnreadNotifications(Integer userId) {
        List<Notification> Notifications = repository.findByUser_IdAndReadAtIsNullOrderByCreatedAtDesc(userId);

        return Notifications
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}