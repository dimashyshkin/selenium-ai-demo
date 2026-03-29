package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public static String getBaseUrl() {
        return System.getProperty("base.url", properties.getProperty("base.url", "https://example.com"));
    }

    public static String getBrowser() {
        return System.getProperty("browser", properties.getProperty("browser", "chrome"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", properties.getProperty("headless", "false")));
    }
}
