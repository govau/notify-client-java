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

        Iterator<Object> iterator = notificationsData.iterator();
        while(iterator.hasNext()) {
             JSONObject notification = (JSONObject)iterator.next();

            notifications.add(new Notification(notification));
        }
    }
}
