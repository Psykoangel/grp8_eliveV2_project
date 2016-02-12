package fr.group8.elive.models;

import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.PrimaryKey;
import ru.noties.storm.anno.Table;

/**
 * Created by psyko on 09/02/16.
 */
@Table("cma")
public class CMAItem {
    @Column("cma_id")
    @PrimaryKey
    private int id;

    @Column("cma_code1")
    private String code1;

    @Column("cma_code2")
    private String code2;

    @Column("cma_level")
    private int level;

    @Column("cma_value")
    private String value;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CMAItem{" +
                "id=" + id +
                ", code1='" + code1 + '\'' +
                ", code2='" + code2 + '\'' +
                ", level=" + level +
                ", value='" + value + '\'' +
                '}';
    }

    public boolean isValid() {
        return !this.getValue().isEmpty() && !this.getCode1().isEmpty() && !this.getCode2().isEmpty();
    }
}
