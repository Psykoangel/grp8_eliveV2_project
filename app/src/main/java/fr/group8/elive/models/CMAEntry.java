package fr.group8.elive.models;

import java.util.Date;

/**
 * Created by psyko on 09/02/16.
 */
public class CMAEntry {

    private int key;
    private Date value;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CMAEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
