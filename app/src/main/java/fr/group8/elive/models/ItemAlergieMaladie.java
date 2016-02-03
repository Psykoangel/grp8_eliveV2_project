package fr.group8.elive.models;

import java.util.Date;

/**
 * Created by chriis on 03/02/2016.
 */
public class ItemAlergieMaladie {
    private String sNomMaladie;
    private Date dDateMaladie;

    public ItemAlergieMaladie(String NomMaladie, Date DateMaladie){

        this.sNomMaladie = NomMaladie;
        this.dDateMaladie = DateMaladie;
    }

    public String getsNomMaladie(){
        return sNomMaladie;
    }

    public void setsNomMaladie(String sNomMaladie) {
        this.sNomMaladie = sNomMaladie;
    }

    public Date getdDateMaladie() {
        return dDateMaladie;
    }

    public void setdDateMaladie(Date dDateMaladie) {
        this.dDateMaladie = dDateMaladie;
    }
}
