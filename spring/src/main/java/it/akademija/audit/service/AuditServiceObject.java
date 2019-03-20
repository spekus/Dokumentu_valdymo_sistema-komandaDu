package it.akademija.audit.service;

import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import java.time.LocalDateTime;

public class AuditServiceObject {

    private String username;
    private String firstname;
    private String lastname;
    private LocalDateTime date;
    private AuditActionEnum action;
    private ObjectTypeEnum objectType;
    private String objectIdentifier;

    public AuditServiceObject(){

    }

    public AuditServiceObject(String username, String firstname, String lastname, LocalDateTime actionDate, AuditActionEnum action, ObjectTypeEnum objectType, String objectIdentifier) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = actionDate;
        this.action = action;
        this.objectType = objectType;
        this.objectIdentifier = objectIdentifier;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AuditActionEnum getAction() {
        return action;
    }

    public ObjectTypeEnum getObjectType() {
        return objectType;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAction(AuditActionEnum action) {
        this.action = action;
    }

    public void setObjectType(ObjectTypeEnum objectType) {
        this.objectType = objectType;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }
}


