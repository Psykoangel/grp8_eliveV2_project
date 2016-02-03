package fr.group8.elive.models;

import java.util.Date;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.DBNonNull;
import ru.noties.storm.anno.Index;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by psyko on 20/01/16.
 */
@Table("user")
public class DataUser {
    @Column
    @PrimaryKey
    @Autoincrement
    private long id;

    @Column
    @DBNonNull
    @Index("uniqId")
    private String uniqId;

    @Column
    @DBNonNull
    private String fileLocation;

    @Column
    @DBNonNull
    private Date lastUpdate;

    public DataUser(String uniqId) {
        this.setUniqId(uniqId);
        this.setLastUpdate(new Date());
    }

    public DataUser(String uniqId, String filename) {
        this(uniqId);
        this.setFileLocation(filename);
    }

    public long getId() {
        return id;
    }

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
