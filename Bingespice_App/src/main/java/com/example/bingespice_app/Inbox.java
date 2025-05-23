// In Inbox.java
package com.example.bingespice_app;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.sql.Timestamp;
import java.util.List;

public class Inbox {
    @FXML private VBox notificationsVBox;

    @FXML
    public void initialize() {
        int userId = Session.getUserID();
        List<Notification> notifications = NotificationsHandler.getNotifications(userId);

        notificationsVBox.getChildren().clear();

        if (notifications.isEmpty()) {
            Text emptyMsg = new Text("Your inbox is empty.");
            emptyMsg.setStyle("-fx-fill: #FD6108; -fx-font-size: 16;");
            notificationsVBox.getChildren().add(emptyMsg);
        } else {
            for (Notification notification : notifications) {
                Button notificationBtn = new Button();
                notificationBtn.getStyleClass().add("notification-button");
                notificationBtn.setAlignment(Pos.CENTER_LEFT);
                notificationBtn.setMaxWidth(Double.MAX_VALUE);

                String text = String.format("%s | %s | %s",
                        notification.getDate(), notification.getStatus(), notification.getContent());
                notificationBtn.setText(text);

                if ("Pending".equals(notification.getStatus())) {
                    notificationBtn.setOnAction(event -> handleNotificationClick(notification));
                }

                notificationsVBox.getChildren().add(notificationBtn);
            }
        }
    }

    private void handleNotificationClick(Notification notification) {
        boolean success = BingespiceDBManager.updateNotificationStatus(
                Session.getUserID(),
                notification.getContent(),
                notification.getDate()
        );

        if (success) {
            initialize(); // Refresh the inbox
        } else {
            Text errorMsg = new Text("Failed to mark as read.");
            errorMsg.setStyle("-fx-fill: red; -fx-font-size: 16;");
            notificationsVBox.getChildren().add(errorMsg);
        }
    }

    @FXML
    private void handleClear() {
        int userId = Session.getUserID();
        boolean success = NotificationsHandler.deleteNotifications(userId);
        notificationsVBox.getChildren().clear();
        Text msg = new Text(success ? "Your inbox is empty." : "Failed to clear.");
        msg.setStyle("-fx-fill: " + (success ? "#FD6108" : "red") + "; -fx-font-size: 16;");
        notificationsVBox.getChildren().add(msg);
    }
}