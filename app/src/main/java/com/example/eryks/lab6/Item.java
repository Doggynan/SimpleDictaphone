package com.example.eryks.lab6;

import java.util.Date;

public class Item {
    private byte[] content;
    private String name;
    private String surname;
    private String title;
    private String info;
    private Date date;
    public Item()
    {}
    public Item(byte[] content) {
        this.content = content;
    }

    public void addContent(byte[] add) {
        if (content == null) {
            setContent(add);
        } else {
            byte[] res = new byte[content.length + add.length];
            for (int i = 0; i < res.length; i++) {
                if (i >= content.length) {
                    res[i] = add[i - content.length];
                } else {
                    res[i] = content[i];
                }
            }
            content = res;
        }
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
