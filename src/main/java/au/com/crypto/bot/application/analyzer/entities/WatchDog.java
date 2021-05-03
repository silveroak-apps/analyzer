package au.com.crypto.bot.application.analyzer.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailaja on 2/23/2018.
 */

@Entity
@Table(name = "watchdog")
public class WatchDog implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "resource")
    private String resource;

    @Column(name = "last_update")
    private Date last_update;


    private WatchDog() {
    }

    public WatchDog(String resource, Date last_update) {
        this.resource = resource;
        this.last_update = last_update;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
