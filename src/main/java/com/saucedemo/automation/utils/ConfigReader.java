package com.saucedemo.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration properties
 */
public class ConfigReader {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties;
    
    /**
     * Private constructor to implement singleton pattern
     */
    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }
    
    /**
     * Get singleton instance
     *
     * @return ConfigReader instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    
    /**
     * Load properties from config file
     */
    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration properties loaded successfully");
            } else {
                logger.error("config.properties file not found in the classpath");
            }
        } catch (IOException e) {
            logger.error("Error loading config.properties", e);
        }
    }
    
    /**
     * Get property value by key
     *
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value by key with default value
     *
     * @param key          Property key
     * @param defaultValue Default value to return if key not found
     * @return Property value or defaultValue if not found
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set property value
     *
     * @param key   Property key
     * @param value Property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}