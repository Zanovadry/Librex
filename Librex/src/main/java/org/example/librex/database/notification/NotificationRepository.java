package org.example.librex.database.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    //list all user notifications
    List<Notification> findByUser_IdOrderByCreatedAtDesc(Integer userId);

    //list only unread user notifications
    List<Notification> findByUser_IdAndReadAtIsNullOrderByCreatedAtDesc(Integer userId);
}
