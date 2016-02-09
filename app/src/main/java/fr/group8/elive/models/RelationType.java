package fr.group8.elive.models;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.DBNonNull;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by psyko on 09/02/16.
 */
@Table("relationtype")
public class RelationType {
    @Column("relationtype_id")
    @PrimaryKey
    @Autoincrement
    private int id;

    @Column("relationtype_name")
    @DBNonNull
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RelationType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
