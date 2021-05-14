package au.com.crypto.bot.application.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Sailaja on 2/27/2018.
 */
public class PropertyUtil {
    private static final Logger logger = Logger.getLogger(PropertyUtil.class);
    public static Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<String, String>();
        Properties prop = new Properties();
        InputStream inputStream = null;

        try {
            String path = System.getProperty("app.props");
            inputStream = new FileInputStream(path);
            //inputStream = getClass().getClassLoader().getResourceAsStream("app_simulation.properties");

            // load a properties file
            prop.load(inputStream);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                properties.put(key, value);
                logger.info("Key : " + key + ", Value : " + value);
            }


        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }

        }
        return properties;
    }

}

