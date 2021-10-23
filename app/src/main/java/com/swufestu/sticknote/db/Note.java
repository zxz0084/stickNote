package com.swufestu.sticknote.db;

import java.io.Serializable;

public class Note implements Serializable {
    private int id;
    private String title;
    private String note;
    private String createDate;
    private String mark;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
}
