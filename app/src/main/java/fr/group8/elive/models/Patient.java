package fr.group8.elive.models;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.group8.elive.utils.StorageManager;

/**
 * Created by chriis on 03/02/2016.
 */
public class Patient {
    private String uniqId;
    private Information information = null;
    private List<Relation> listRelation = null;
    private List<AlergieMaladie> listAlergieMaladie = null;

    public Patient() {}

    public Patient(User user) {
        this(user, null);
    }

    public Patient(User user, List<User> relations) {
        this.setId(user.getUserUniqId());

        Information info = new Information(
                user.getPersonalData().getPersonaldataUsername(),
                user.getPersonalData().getPersonaldataUserfirstname(),
                user.getPersonalData().getPersonaldataAddress(),
                StorageManager.Instance().selectBloodGroup(user.getPersonalData().getBloodgroupId()),
                user.getUserUniqId(),
                user.getPersonalData().getPersonaldataPhonenumber()
        );

        this.setInformation(info);

        if (relations != null
                && !relations.isEmpty()) {
            this.setListRelation(new ArrayList<Relation>());
            for (int i = 0; i < user.getRelationships().size(); i++) {
                this.getListRelation().add(
                    new Relation(
                        relations.get(i).getPersonalData().getPersonaldataUsername(),
                        relations.get(i).getPersonalData().getPersonaldataUserfirstname(),
                        StorageManager.Instance().selectRelationType(user.getRelationships().get(i).getRelationTypeId())
                    )
                );
            }
        }

        if (user != null
                && user.getPersonalData().getCmas() != null
                && !user.getPersonalData().getCmas().getEntries().isEmpty()) {
            this.setListAlergieMaladie(new ArrayList<AlergieMaladie>());
            for (CMAEntry e : user.getPersonalData().getCmas().getEntries()) {
                this.getListAlergieMaladie().add(
                    new AlergieMaladie(
                        StorageManager.Instance().selectCMA(e.getKey()),
                        e.getValue()
                    )
                );
            }
        }
    }

    public String getId(){ return uniqId; }
    public Information getInformation() { return information; }
    public List<AlergieMaladie> getListAlergieMaladie() { return listAlergieMaladie; }
    public List<Relation> getListRelation() { return listRelation; }

    public void setId(String id){ this.uniqId = id; }
    public void setInformation(Information information) { this.information = information; }
    public void setListAlergieMaladie(List<AlergieMaladie> listAlergieMaladie) { this.listAlergieMaladie = listAlergieMaladie; }
    public void setListRelation(List<Relation> listRelation) { this.listRelation = listRelation; }

    //**********************************************
    //**************Object Information**************
    //**********************************************
    public static class Information{
        private String sNomPatient;
        private String sPrenomPatient;
        private String sAdresse;
        private String sGroupeSanguin;
        private String sId;
        private String sTel;




        public Information(String sNomPatient, String sPrenomPatient, String sAdresse, String sGroupeSanguin,String sId,String sTel){
            this.sNomPatient = sNomPatient;
            this.sAdresse = sAdresse;
            this.sPrenomPatient = sPrenomPatient;
            this.sGroupeSanguin = sGroupeSanguin;
            this.sId = sId;
            this.sTel = sTel;

        }

        public String getsAdresse() {
            return sAdresse;
        }

        public String getsGroupeSanguin() {
            return sGroupeSanguin;
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

        public void setsGroupeSanguin(String sGroupeSanguin) {
            this.sGroupeSanguin = sGroupeSanguin;
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

        public String getsNomRelation() { return sNomRelation; }
        public String getsPrenomRelation() { return sPrenomRelation; }
        public String getsType() { return sType; }
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

        public Date getdDate() { return dDate; }

        public String getsAlergieMaladie() { return sAlergieMaladie; }
    }


}
