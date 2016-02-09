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
    private List<Traitement> listTraitement;
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

    public Patient(Information information, List<Traitement> listTraitement, List<AlergieMaladie> listAlergieMaladie){
        this.uniqId = null;
        this.listTraitement = listTraitement;
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

    public List<Traitement> getListTraitement() {
        return listTraitement;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public void setListAlergieMaladie(List<AlergieMaladie> listAlergieMaladie) {
        this.listAlergieMaladie = listAlergieMaladie;
    }

    public void setListTraitement(List<Traitement> listTraitement) {
        this.listTraitement = listTraitement;
    }


    //**********************************************
    //**************Object Information**************
    //**********************************************


    public static class Information{
        private String sNomPatient;
        private String sPrenomPatient;
        private String sAdresse;
        private String sGrouppeSanguin;
        private String sNomPerePatient;
        private String sPrenomPerePatient;
        private String sNomMerePatient;
        private String sPrenomMerePatient;


        public Information(String sNomPatient, String sPrenomPatient, String sAdresse, String sGrouppeSanguin, String sNomPerePatient,String sPrenomPerePatient,String sNomMerePatient,String sPrenomMerePatient){
            this.sNomPatient = sNomPatient;
            this.sAdresse = sAdresse;
            this.sPrenomPatient = sPrenomPatient;
            this.sGrouppeSanguin = sGrouppeSanguin;
            this.sNomPerePatient = sNomPerePatient;
            this.sPrenomPerePatient = sPrenomPerePatient;
            this.sNomMerePatient =sNomMerePatient;
            this.sPrenomMerePatient = sPrenomMerePatient;
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

        public String getsNomMerePatient() {
            return sNomMerePatient;
        }

        public String getsNomPerePatient() {
            return sNomPerePatient;
        }

        public String getsPrenomMerePatient() {
            return sPrenomMerePatient;
        }

        public String getsPrenomPerePatient() {
            return sPrenomPerePatient;
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

        public void setsNomMerePatient(String sNomMerePatient) {
            this.sNomMerePatient = sNomMerePatient;
        }

        public void setsNomPerePatient(String sNomPerePatient) {
            this.sNomPerePatient = sNomPerePatient;
        }

        public void setsPrenomMerePatient(String sPrenomMerePatient) {
            this.sPrenomMerePatient = sPrenomMerePatient;
        }

        public void setsPrenomPerePatient(String sPrenomPerePatient) {
            this.sPrenomPerePatient = sPrenomPerePatient;
        }

    }


    //*********************************************
    //**************Object Traitement**************
    //*********************************************



    public static class Traitement{
        private String sMedicament;
        public Traitement(String sMedicament){
            this.sMedicament = sMedicament;
        }

        public String getsMedicament() {
            return sMedicament;
        }

        public void setsMedicament(String sMedicament) {
            this.sMedicament = sMedicament;
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
