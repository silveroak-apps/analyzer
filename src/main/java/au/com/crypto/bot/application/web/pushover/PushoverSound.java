package au.com.crypto.bot.application.web.pushover;

public class PushoverSound {

    private final String id;
    private final String name;

    public PushoverSound(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
