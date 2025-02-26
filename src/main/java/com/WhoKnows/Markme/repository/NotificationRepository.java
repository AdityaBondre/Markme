package com.WhoKnows.Markme.repository;

import com.WhoKnows.Markme.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :expiryTime")
    void deleteAllExpiredNotifications(LocalDateTime expiryTime);

    List<Notification> findByTarget(String target);

    List<Notification> findBySection(String section);
}
