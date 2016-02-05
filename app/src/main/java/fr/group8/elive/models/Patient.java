package fr.group8.elive.models;

import java.util.Date;
import java.util.List;

/**
 * Created by chriis on 03/02/2016.
 */
public class Patient {
    private Integer id;
    private Information information;
    private List<Traitement> listTraitement;
    private List<AlergieMaladie> listAlergieMaladie;


    public Patient(){
    }

    public Patient(Information information, List<Traitement> listTraitement, List<AlergieMaladie> listAlergieMaladie){
        this.id = 0;
        this.listTraitement = listTraitement;
        this.information = information;
        this.listAlergieMaladie = listAlergieMaladie;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
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


    public class Information{
        private String sNomPatient;
        private String sPrenomPatient;
        private String sAdresse;
        private String sGrouppeSanguin;

        public Information(String sNomPatient, String sPrenomPatient, String sAdresse, String sGrouppeSanguin){
            this.sNomPatient = sNomPatient;
            this.sAdresse = sAdresse;
            this.sPrenomPatient = sPrenomPatient;
            this.sGrouppeSanguin = sGrouppeSanguin;
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
    }


    //*********************************************
    //**************Object Traitement**************
    //*********************************************



    public class Traitement{
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



    public class AlergieMaladie{
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
