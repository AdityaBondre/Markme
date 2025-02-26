package com.WhoKnows.Markme.service;

import com.WhoKnows.Markme.model.Notification;
import com.WhoKnows.Markme.model.Professor;
import com.WhoKnows.Markme.repository.NotificationRepository;
import com.WhoKnows.Markme.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    //create notification
    public Notification createNotification(String message, String username, String target, String section) {
        Optional<Professor> professor = professorRepository.findByUsername(username);
        Long professorId = professor.get().getId();
        Notification notification = new Notification(message, professorId, target, section,username);
        return notificationRepository.save(notification);
    }

    //list of notifications
    public List<Notification> getNotificationsForAllAndUserSection(String sectionName) {
        // Get notifications for "ALL"
        List<Notification> allNotifications = notificationRepository.findByTarget("ALL");

        // Get notifications for the user's specific section
        List<Notification> sectionNotifications = notificationRepository.findBySection(sectionName);

        // Combine both lists
        allNotifications.addAll(sectionNotifications);
        // Reverse the order of the list
        Collections.reverse(allNotifications);

        return allNotifications;
    }


    //list of all notifications
    public List<Notification> getNotificationsForAll() {
        return notificationRepository.findByTarget("ALL");
    }

    //list of specific section notifications
    public List<Notification> getNotificationsForSection(String section) {
        return notificationRepository.findBySection(section);
    }

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void deleteExpiredNotifications() {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(24);
        notificationRepository.deleteAllExpiredNotifications(expiryTime);
    }
}