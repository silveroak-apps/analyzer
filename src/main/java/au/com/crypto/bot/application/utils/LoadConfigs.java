package au.com.crypto.bot.application.utils;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.analyzer.entities.Configs;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadConfigs {
    static Gson gson = new Gson();
    public static Map<String, String> getConfig(ApplicationControllers ac, String config) {
        List<Configs> configs =  ac.getConfigController().findByName(config);
        if (!config.isEmpty()) {
            String jsonString = configs.get(0).getJsonValue();
            Configurations contractsConfiguration = gson.fromJson(jsonString, Configurations.class);
            return Arrays.stream(contractsConfiguration.getConfiguration()).collect(
                    Collectors.toMap(Configurations.Configuration::getKey, Configurations.Configuration::getValue));
        }
        return Collections.emptyMap();
    }
}
