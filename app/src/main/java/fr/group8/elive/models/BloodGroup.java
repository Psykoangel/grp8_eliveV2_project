package fr.group8.elive.models;

import ru.noties.storm.anno.Autoincrement;
import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.DBNonNull;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by psyko on 09/02/16.
 */
@Table("bloodgroup")
public class BloodGroup {
    @Column("bloodgroup_id")
    @PrimaryKey
    @Autoincrement
    private int id;

    @Column("bloodgroup_name")
    @DBNonNull
    private String name;

    @Column("bloodgroup_sign")
    @DBNonNull
    private String sign;


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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "BloodGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public boolean isValide() {
        return !getName().isEmpty() && !getSign().isEmpty();
    }
}
