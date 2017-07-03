package com.rainbow.red_alert.monitor.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

public class ConfigService {
    private static final String CONFIG_FILE_NAME = "config.yml";

    private static Configuration configuration;

    static {
        try {
            initConfiguration();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void initConfiguration() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        configuration = mapper.readValue(ConfigService.class.getClassLoader()
                                                            .getResourceAsStream(CONFIG_FILE_NAME), Configuration.class);
    }
}
