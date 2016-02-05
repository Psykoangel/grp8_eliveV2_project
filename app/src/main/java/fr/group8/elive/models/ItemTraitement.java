package fr.group8.elive.models;

/**
 * Created by chriis on 03/02/2016.
 */
public class ItemTraitement {
    private String sNomTraitement;

    public  ItemTraitement (String NomTraitement){
        this.sNomTraitement = NomTraitement;
    }

    public String getsNomTraitement() {
        return sNomTraitement;
    }

    public void setsNomTraitement(String sNomTraitement) {
        this.sNomTraitement = sNomTraitement;
    }
}
