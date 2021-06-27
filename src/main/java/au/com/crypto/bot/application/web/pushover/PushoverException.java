package au.com.crypto.bot.application.web.pushover;

public class PushoverException extends Exception {

    private static final long serialVersionUID = 1L;

    public PushoverException(String message, Throwable cause) {
        super(message, cause);
    }
}
