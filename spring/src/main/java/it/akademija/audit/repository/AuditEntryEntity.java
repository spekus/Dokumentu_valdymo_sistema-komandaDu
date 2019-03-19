package it.akademija.audit.repository;

import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuditEntryEntities")

//indexes and namequeries

public class AuditEntryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="firstname", nullable = false)
    private String firstname;

    @Column(name="lastname", nullable = false)
    private String lastname;

    @Column(name="action_date", nullable = false)
    private LocalDateTime date;

    @Column(name="action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditActionEnum action;

    @Column(name="object_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectTypeEnum objectType;

    @Column(name="object_Identifier", nullable = false)
//    @Enumerated(EnumType.STRING)
    private String objectIdentifier;

    public AuditEntryEntity(){
    }

    public AuditEntryEntity(String username, String firstname, String lastname, LocalDateTime date, AuditActionEnum action,
                            ObjectTypeEnum objectType, String objectIdentifier) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.action = action;
        this.objectType = objectType;
        this.objectIdentifier = objectIdentifier;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public AuditActionEnum getAction() {
        return action;
    }

    public void setAction(AuditActionEnum action) {
        this.action = action;
    }

    public ObjectTypeEnum getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypeEnum objectType) {
        this.objectType = objectType;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }
}
