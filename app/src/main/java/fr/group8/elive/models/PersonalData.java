package fr.group8.elive.models;

import java.util.Date;
import java.util.List;

/**
 * Created by psyko on 09/02/16.
 */
public class PersonalData {
    private int bloodgroupId;
    private String personaldataUserfirstname;
    private String personaldataUsername;
    private Date personaldataBirthdate;
    private String personaldataPhonenumber;
    private String personaldataAddress;
    private String personaldataAdditional;
    private CMA cmas;

    public int getBloodgroupId() {
        return bloodgroupId;
    }

    public void setBloodgroupId(int bloodgroupId) {
        this.bloodgroupId = bloodgroupId;
    }

    public String getPersonaldataUserfirstname() {
        return personaldataUserfirstname;
    }

    public void setPersonaldataUserfirstname(String personaldataUserfirstname) {
        this.personaldataUserfirstname = personaldataUserfirstname;
    }

    public String getPersonaldataUsername() {
        return personaldataUsername;
    }

    public void setPersonaldataUsername(String personaldataUsername) {
        this.personaldataUsername = personaldataUsername;
    }

    public Date getPersonaldataBirthdate() {
        return personaldataBirthdate;
    }

    public void setPersonaldataBirthdate(Date personaldataBirthdate) {
        this.personaldataBirthdate = personaldataBirthdate;
    }

    public String getPersonaldataPhonenumber() {
        return personaldataPhonenumber;
    }

    public void setPersonaldataPhonenumber(String personaldataPhonenumber) {
        this.personaldataPhonenumber = personaldataPhonenumber;
    }

    public String getPersonaldataAddress() {
        return personaldataAddress;
    }

    public void setPersonaldataAddress(String personaldataAddress) {
        this.personaldataAddress = personaldataAddress;
    }

    public String getPersonaldataAdditional() {
        return personaldataAdditional;
    }

    public void setPersonaldataAdditional(String personaldataAdditional) {
        this.personaldataAdditional = personaldataAdditional;
    }

    public CMA getCmas() {
        return cmas;
    }

    public void setCmas(CMA cmas) {
        this.cmas = cmas;
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                "bloodgroupId=" + bloodgroupId +
                ", personaldataUserfirstname='" + personaldataUserfirstname + '\'' +
                ", personaldataUsername='" + personaldataUsername + '\'' +
                ", personaldataBirthdate=" + personaldataBirthdate +
                ", personaldataPhonenumber='" + personaldataPhonenumber + '\'' +
                ", personaldataAddress='" + personaldataAddress + '\'' +
                ", personaldataAdditional='" + personaldataAdditional + '\'' +
                ", cmas=" + cmas +
                '}';
    }
}
