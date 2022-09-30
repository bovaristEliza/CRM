package model.mo;

import java.sql.Timestamp;
import java.util.Date;

public class Notes {

    private Integer noteid;
    private String content;
    private java.sql.Timestamp noteDate;
    private Integer manager;
    private Integer customer;
    private  String deleted;


    /////////////////////////////

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public void setNoteDate(Timestamp noteDate) {
        this.noteDate = noteDate;
    }

    public Timestamp getNoteDate() {
        return noteDate;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }


}
