package fr.group8.elive.models;

import java.util.Date;
import java.util.List;

/**
 * Created by psyko on 03/02/16.
 */
public class User {

    private int userId;
    private int personaldataId;
    private String userUniqId;
    private boolean userIsDeleted;
    private Date userUpdatedate;
    private Date userCreationdate;
    private List<Relationship> relationships;
    private PersonalData personalData;


    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPersonaldataId() {
        return personaldataId;
    }

    public void setPersonaldataId(int personaldataId) {
        this.personaldataId = personaldataId;
    }

    public String getUserUniqId() {
        return userUniqId;
    }

    public void setUserUniqId(String userUniqId) {
        this.userUniqId = userUniqId;
    }

    public boolean isUserIsDeleted() {
        return userIsDeleted;
    }

    public void setUserIsDeleted(boolean userIsDeleted) {
        this.userIsDeleted = userIsDeleted;
    }

    public Date getUserUpdatedate() {
        return userUpdatedate;
    }

    public void setUserUpdatedate(Date userUpdatedate) {
        this.userUpdatedate = userUpdatedate;
    }

    public Date getUserCreationdate() {
        return userCreationdate;
    }

    public void setUserCreationdate(Date userCreationdate) {
        this.userCreationdate = userCreationdate;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", personaldataId=" + personaldataId +
                ", userUniqId='" + userUniqId + '\'' +
                ", userIsDeleted=" + userIsDeleted +
                ", userUpdatedate=" + userUpdatedate +
                ", userCreationdate=" + userCreationdate +
                ", relationships=" + relationships +
                ", personalData=" + personalData +
                '}';
    }
}
