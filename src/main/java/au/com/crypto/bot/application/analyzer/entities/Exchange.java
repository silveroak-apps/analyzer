package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "EXCHANGE")
public class Exchange implements Serializable {

    private static final long serialVersionUID = -3009157732242241506L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MORE_INFO")
    private String moreInfo;


    public Exchange() {
    }

    public Exchange(String code, String description,  String moreInfo) {
        this.code = code;
        this.description = description;
        this.moreInfo = moreInfo;//9985743479
    }

    @Override
    public String toString() {
        return String.format("Exchange[id=%d, code='%s', description='%s', moreInfo = %s]", id, code, description,moreInfo);
    }
}
