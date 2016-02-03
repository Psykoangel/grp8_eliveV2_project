package fr.group8.elive.models;

import java.util.List;

/**
 * Created by chriis on 03/02/2016.
 */
public class Patient {
    private Integer id;
    private String patientForname;
    private String patientName;
    private List<RelationShip> relationshipList;
    private List<CmaObject> patientCmaList;

    public Patient(){
    }

    public Patient(String patientForname, String patientName){
        this.id = 0;
        this.patientForname = patientForname;
        this.patientName = patientName;
    }

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getUserForname()
    {
        return patientForname;
    }
    public void setUserForname(String userForname)
    {
        this.patientForname = userForname;
    }
    public String getUserName()
    {
        return patientName;
    }
    public void setUserName(String userName)
    {
        this.patientName = userName;
    }
    public List<RelationShip> getRelationshipList()
    {
        return relationshipList;
    }
    public void setRelationshipList(List<RelationShip> relationshipList)
    {
        this.relationshipList = relationshipList;
    }

    public List<CmaObject> getUserCmaList() {
        return patientCmaList;
    }

    public void setUserCmaList(List<CmaObject> userCmaList) {
        this.patientCmaList = userCmaList;
    }

    public Boolean validate(){
        return !(patientName.isEmpty() || patientForname.isEmpty());
    }

    @Override
    public String toString()
    {
        return "User [id=" + id + ", userForname=" + patientForname + ", " +
                "userName=" + patientName + ", relationShips= [" + relationshipList + "], cma=[" + patientCmaList + "]]";
    }

    public class RelationShip {
        private String entourageForname;
        private String entourageName;
        private Integer relationshipTypeCode;

        public RelationShip(String forname, String name, Integer type){
            this.entourageForname = forname;
            this.entourageName = name;
            this.relationshipTypeCode = type;
        }

        public RelationShip() {

        }

        public String getEntourageForname() {
            return entourageForname;
        }

        public String getEntourageName() {
            return entourageName;
        }

        public String getRelationshipTypeCode() {
            return relationshipTypeCode == 11 ? "Père" : "Mère";
        }

        public void setEntourageForname(String entourageForname) {
            this.entourageForname = entourageForname;
        }

        public void setEntourageName(String entourageName) {
            this.entourageName = entourageName;
        }

        public void setRelationshipTypeCode(Integer relationshipTypeCode) {
            this.relationshipTypeCode = relationshipTypeCode;
        }

        @Override
        public String toString()
        {
            return "RelationShip [entourageForname=" + entourageForname + ", entourageName=" + entourageName + ", " +
                    "relationshipTypeCode=" + this.getRelationshipTypeCode() + "]";
        }
    }

    public class CmaObject {
        private String cmaCode1;
        private String cmaCode2;
        private String cmaLevel;
        private String cmaValue;

        public CmaObject(String code1, String code2, String level, String value){
            this.cmaCode1 = code1;
            this.cmaCode2 = code2;
            this.cmaLevel = level;
            this.cmaValue = value;
        }

        public CmaObject() {

        }

        public String getCmaCode1() {
            return cmaCode1;
        }

        public void setCmaCode1(String cmaCode1) {
            this.cmaCode1 = cmaCode1;
        }

        public String getCmaCode2() {
            return cmaCode2;
        }

        public void setCmaCode2(String cmaCode2) {
            this.cmaCode2 = cmaCode2;
        }

        public String getCmaLevel() {
            return cmaLevel;
        }

        public void setCmaLevel(String cmaLevel) {
            this.cmaLevel = cmaLevel;
        }

        public String getCmaValue() {
            return cmaValue;
        }

        public void setCmaValue(String cmaValue) {
            this.cmaValue = cmaValue;
        }

        @Override
        public String toString() {
            return "CmaObject [cmaCode1=" + cmaCode1 + ", cmaCode2=" + cmaCode2 + ", " +
                    "cmaLevel=" + cmaLevel + ", cmaValue=" + cmaValue + "]";
        }
    }
}
