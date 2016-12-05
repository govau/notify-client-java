package uk.gov.service.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.json.JSONArray;

public class NotificationList {
    private List<Notification> notifications;
    private String currentPageLink;
    private Optional<String> nextPageLink;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public Optional<String> getNextPageLink() {
        return nextPageLink;
    }

    public String getCurrentPageLink() {
        return currentPageLink;
    }


    public NotificationList(String content){
        JSONObject data = new JSONObject(content);
        JSONObject links = data.getJSONObject("links");
        currentPageLink = links.isNull("current") ? null : links.getString("current");
        nextPageLink = links.isNull("next") ? Optional.<String>empty() : Optional.of(links.getString("next"));
        notifications =  new ArrayList<>();

        JSONArray notificationsData = data.getJSONArray("notifications");
        for(int i = 0; i < notificationsData.length(); i++){
            JSONObject notification = notificationsData.getJSONObject(i);
            notifications.add(new Notification(notification));
        }
    }

    @Override
    public String toString() {
        StringBuilder notifications_string = new StringBuilder("\n");
        for (Notification notification : notifications){
            notifications_string.append(notification.toString()).append("\n");
        }
        return "NotificationList{" +
                "notifications=" + notifications_string.toString() +
                ", currentPageLink='" + currentPageLink + '\'' +
                ", nextPageLink='" + nextPageLink + '\'' +
                '}';
    }
}
