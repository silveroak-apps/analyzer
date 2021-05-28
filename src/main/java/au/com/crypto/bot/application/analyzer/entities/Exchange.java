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

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    public Exchange() {
    }

    public Exchange(String code, String name, boolean active) {
        this.code = code;
        this.name = name;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return String.format("Exchange[id=%d, code='%s', description='%s', moreInfo = %s]", id, code, name);
    }
}
