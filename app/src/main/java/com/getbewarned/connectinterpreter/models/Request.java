package com.getbewarned.connectinterpreter.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Request extends RealmObject {
    @PrimaryKey
    private long id;
    private Date created;
    private Date updated;
    private int unread_count = 0;
    private String name;
    private String status;

    private RealmList<RequestMessage> messages;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getUnreadCount() {
        return unread_count;
    }

    public void setUnreadCount(int unread_count) {
        this.unread_count = unread_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RealmList<RequestMessage> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<RequestMessage> messages) {
        this.messages = messages;
    }
}
