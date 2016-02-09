package fr.group8.elive.models;

import java.util.Date;
import java.util.List;

import fr.group8.elive.utils.StorageManager;

/**
 * Created by chriis on 03/02/2016.
 */
public class Patient {
    private String uniqId;
    private Information information;
    private List<Relation> listRelation;
    private List<AlergieMaladie> listAlergieMaladie;


    public Patient(){
    }

    public Patient(User user) {
        this.setId(user.getUserUniqId());
        /*Information info = new Information(
                user.getPersonalData().getPersonaldataUsername(),
                user.getPersonalData().getPersonaldataUserfirstname(),
                user.getPersonalData().getPersonaldataAddress(),
                StorageManager.Instance().selectBloodGroup(user.getPersonalData().getBloodgroupId()),
                null,
                null,
                null,
                null
        );*/
    }

    public Patient(Information information, List<Relation> listRelation, List<AlergieMaladie> listAlergieMaladie){
        this.uniqId = null;
        this.listRelation = listRelation;
        this.information = information;
        this.listAlergieMaladie = listAlergieMaladie;
    }

    public String getId(){
        return uniqId;
    }

    public void setId(String id){
        this.uniqId = id;
    }

    public Information getInformation() {
        return information;
    }

    public List<AlergieMaladie> getListAlergieMaladie() {
        return listAlergieMaladie;
    }

    public List<Relation> getListRelation() {
        return listRelation;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setListAlergieMaladie(List<AlergieMaladie> listAlergieMaladie) {
        this.listAlergieMaladie = listAlergieMaladie;
    }

    public void setListTraitement(List<Relation> listRelation) {
        this.listRelation = listRelation;
    }


    //**********************************************
    //**************Object Information**************
    //**********************************************


    public static class Information{
        private String sNomPatient;
        private String sPrenomPatient;
        private String sAdresse;
        private String sGrouppeSanguin;
        private String sId;
        private String sTel;




        public Information(String sNomPatient, String sPrenomPatient, String sAdresse, String sGrouppeSanguin,String sId,String sTel){
            this.sNomPatient = sNomPatient;
            this.sAdresse = sAdresse;
            this.sPrenomPatient = sPrenomPatient;
            this.sGrouppeSanguin = sGrouppeSanguin;
            this.sId = sId;
            this.sTel = sTel;

        }

        public String getsAdresse() {
            return sAdresse;
        }

        public String getsGrouppeSanguin() {
            return sGrouppeSanguin;
        }

        public String getsNomPatient() {
            return sNomPatient;
        }

        public String getsPrenomPatient() {
            return sPrenomPatient;
        }

        public String getsId() {
            return sId;
        }

        public String getsTel() {
            return sTel;
        }

        public void setsAdresse(String sAdresse) {
            this.sAdresse = sAdresse;
        }

        public void setsGrouppeSanguin(String sGrouppeSanguin) {
            this.sGrouppeSanguin = sGrouppeSanguin;
        }

        public void setsNomPatient(String sNomPatient) {
            this.sNomPatient = sNomPatient;
        }

        public void setsPrenomPatient(String sPrenomPatient) {
            this.sPrenomPatient = sPrenomPatient;
        }

        public void setsId(String sId) {
            this.sId = sId;
        }

        public void setsTel(String sTel) {
            this.sTel = sTel;
        }
    }


    //*********************************************
    //**************Object Relation**************
    //*********************************************



    public static class Relation{
        private String sNomRelation;
        private String sPrenomRelation;
        private String sType;

        public Relation(String sNomRelation,String sPrenomRelation,String sType){
            this.sNomRelation = sNomRelation;
            this.sPrenomRelation = sPrenomRelation;
            this.sType = sType;
        }

        public String getsNomRelation() {
            return sNomRelation;
        }

        public String getsPrenomRelation() {
            return sPrenomRelation;
        }

        public String getsType() {
            return sType;
        }

        public void setsNomRelation(String sNomRelation) {
            this.sNomRelation = sNomRelation;
        }

        public void setsPrenomRelation(String sPrenomRelation) {
            this.sPrenomRelation = sPrenomRelation;
        }

        public void setsType(String sType) {
            this.sType = sType;
        }
    }

    //************************************************
    //**************Object AlergieMaladie*************
    //************************************************



    public static class AlergieMaladie{
        private String sAlergieMaladie;
        private Date dDate;

        public AlergieMaladie(String sAlergieMaladie,Date dDate){
            this.dDate = dDate;
            this.sAlergieMaladie = sAlergieMaladie;
        }

        public Date getdDate() {
            return dDate;
        }

        public String getsAlergieMaladie() {
            return sAlergieMaladie;
        }

        public void setdDate(Date dDate) {
            this.dDate = dDate;
        }

        public void setsAlergieMaladie(String sAlergieMaladie) {
            this.sAlergieMaladie = sAlergieMaladie;
        }
    }


}
