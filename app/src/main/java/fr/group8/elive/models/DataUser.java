package fr.group8.elive.models;

import android.text.format.DateUtils;

import java.sql.Time;
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

    public DataUser(String uniqId, String filename) {
        this.uniqId = uniqId;
        this.fileLocation = filename;
        this.lastUpdate = new Date();
    }

}
