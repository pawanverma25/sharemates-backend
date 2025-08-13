package dev.pawan.sharemate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.pawan.sharemate.model.Notification;

@Service
public class NotificationService {
	public void sendNotification(List<Notification> le) {
		// Logic to send notification to the user
		// This could involve integrating with a messaging service or using push notifications
//		System.out.println("Sending notification to user " + userId + ": " + message);
	}
}
