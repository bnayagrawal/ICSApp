package xyz.bnayagrawal.android.icsapp.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by binay on 10/9/2017.
 */

public class EventData {
    private int id;
    private String title;
    private String image_url;
    private String description;
    private Date start_time;
    private Date end_time;
    private ArrayList<HashMap<String,String>> attending;
    private ArrayList<HashMap<String,String>> attended;
    private ArrayList<HashMap<String,String>> starred;
    private Date created_at;
    private Date updated_at;
    private String venue;
    private String phone;

    public EventData(int id,
                     String title,
                     String image_url,
                     String description,
                     String venue,
                     String phone,
                     Date start_time,
                     Date end_time,
                     Date created_at,
                     Date updated_at,
                     ArrayList<HashMap<String,String>> attending,
                     ArrayList<HashMap<String,String>> attended,
                     ArrayList<HashMap<String,String>> starred) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.description = description;
        this.venue = venue;
        this.phone = phone;
        this.start_time = start_time;
        this.end_time = end_time;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.attending = attending;
        this.attended = attended;
        this.starred = starred;
    }

    //getter methods
    public int getId() {return this.id;}
    public String getTitle() {return this.title;}
    public String getDescription() {return this.description;}
    public String getImageUrl() {return this.image_url;}
    public String getVenue() {return this.venue;}
    public String getPhone() {return this.phone;}
    public Date getCreated_at() { return this.created_at;}
    public Date getUpdated_at() {return this.updated_at;}
    public Date getStart_time() { return this.start_time;}
    public Date getEnd_time() { return this.end_time;}
    public ArrayList<HashMap<String,String>> getAttending() {return this.attending;}
    public ArrayList<HashMap<String,String>> getAttended() {return this.attended;}
    public ArrayList<HashMap<String,String>> getStarred() {return this.starred;}
}

