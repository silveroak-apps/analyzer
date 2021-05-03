package au.com.crypto.bot.application.utils;

public class Configurations {

    private Configuration[] configuration;

    public Configuration[] getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration[] configuration) {
        this.configuration = configuration;
    }

    public class Configuration {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
