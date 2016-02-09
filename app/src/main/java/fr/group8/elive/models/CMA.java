package fr.group8.elive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psyko on 09/02/16.
 */
public class CMA {
    @SerializedName("entry")
    private List<CMAEntry> entries;

    public List<CMAEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<CMAEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "CMA{" +
                "entries=" + entries +
                '}';
    }
}
