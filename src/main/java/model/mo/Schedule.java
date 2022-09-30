package model.mo;

import java.sql.Timestamp;

public class Schedule {

    private Integer idschedule;
    private Integer noteid;
    private Integer customerid;
    private Integer userid;
    private java.sql.Timestamp dateApp;
    private String deleted;

    //******* foreign keys 1:N **********************************

    private User user;
    private Notes note;
    private Customer customer;

    //***** getters and setters***********************************

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notes getNote() {
        return note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Timestamp getDateApp() {
        return dateApp;
    }

    public void setDateApp(Timestamp dateApp) {
        this.dateApp = dateApp;
    }


    public Integer getIdschedule() {
        return idschedule;
    }

    public void setIdschedule(Integer idschedule) {
        this.idschedule = idschedule;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }


    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }



}
