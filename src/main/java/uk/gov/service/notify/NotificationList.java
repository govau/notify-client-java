package uk.gov.service.notify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class NotificationList {
    private List<Notification> notifications;
    private String nextPageLink;
    private String lastPageLink;
    private int pageSize;
    private int total;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public String getNextPageLink() {
        return nextPageLink;
    }

    public String getLastPageLink() {
        return lastPageLink;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public NotificationList(String content){
        JSONObject data = new JSONObject(content);
        JSONObject links = data.getJSONObject("links");
        nextPageLink = links.isNull("next") ? null : links.getString("next");
        lastPageLink = links.isNull("last") ? null : links.getString("last");
        pageSize = data.getInt("page_size");
        total = data.getInt("total");
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
                ", nextPageLink='" + nextPageLink + '\'' +
                ", lastPageLink='" + lastPageLink + '\'' +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }
}
