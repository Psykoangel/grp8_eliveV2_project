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
}
