package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class NotificationUnreadCountEvent {

    private String notificationUnreadCount = "";

    public NotificationUnreadCountEvent(String notificationUnreadCount) {
        this.notificationUnreadCount = notificationUnreadCount;
    }


    public String getNotificationUnreadCount() {
        return notificationUnreadCount;
    }
}
