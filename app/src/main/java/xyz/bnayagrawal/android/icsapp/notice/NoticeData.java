package xyz.bnayagrawal.android.icsapp.notice;

import java.util.Date;

/**
 * Created by binay on 11/2/2017.
 */

public class NoticeData {
    private String title;
    private Date date;
    private String description;
    private String attachment_url;
    private boolean has_attachment;

    public NoticeData(String title, String description, Date date, boolean has_attachment, String attachment_url) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.has_attachment = has_attachment;
        this.attachment_url= attachment_url;
    }

    //getter methods
    public String getTitle() {return this.title;}
    public String getDescription() {return this.description;}
    public Date getDate() {return this.date;}
    public boolean hasAttachment() {return this.has_attachment;}
    public String getAttachmentUrl() {return this.attachment_url;}
}
