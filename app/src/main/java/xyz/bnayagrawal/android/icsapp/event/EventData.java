package xyz.bnayagrawal.android.icsapp.event;

import java.util.Date;

/**
 * Created by binay on 10/9/2017.
 */

public class EventData {
    private String title;
    private String image; //currently the image resource will be used, so int. later change to string for storing url.
    private String description;
    private Date notification_date;
    private int peoples_interested;
    private int peoples_going;
    private Date event_date;
    private String venue;

    public EventData(String title, String image, String description, Date notification_date, int peoples_interested, int peoples_going, Date event_date, String venue) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.notification_date = notification_date;
        this.peoples_interested = peoples_interested;
        this.peoples_going = peoples_going;
        this.event_date = event_date;
        this.venue = venue;
    }

    //getter methods
    public String getTitle() {return this.title;}
    public String getImage() {return this.image;}
    public String getDescription() {return this.description;}
    public Date getNotification_date() {return this.notification_date;}
    public int getPeoples_interested() {return this.peoples_interested;}
    public int getPeoples_going() {return this.peoples_going;}
    public Date getDate() {return this.event_date;}
    public String getVenue() {return this.venue;}
}

