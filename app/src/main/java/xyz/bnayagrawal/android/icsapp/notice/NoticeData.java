package xyz.bnayagrawal.android.icsapp.notice;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by binay on 11/2/2017.
 */

public class NoticeData {
    private int id;
    private String title;
    private String text;
    private Date created_at,updated_at;
    private HashMap<String,String> users_read;

    public NoticeData(int id, String title, String text, Date created_at, Date updated_at,HashMap<String,String> users_read) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.users_read = users_read;
    }

    //getter methods
    public String getTitle() {return this.title;}
    public String getText() {return this.text;}
    public Date getCreated_at() {return this.created_at;}
    public Date getUpdated_at() {return this.updated_at;}
    public HashMap<String,String> getUsers_read() {return this.users_read;}
}
