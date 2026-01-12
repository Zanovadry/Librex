package org.example.librex.database.notification.dto;

import org.example.librex.database.notification.Notification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private final Integer id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime readAt;
    private final boolean read;

    public NotificationResponse(Integer id,
                                String title,
                                String content,
                                LocalDateTime createdAt,
                                LocalDateTime readAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.read = (readAt != null);
    }

    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getContent(),
                n.getCreatedAt(),
                n.getReadAt()
        );
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public boolean isRead() { return read; }
}
