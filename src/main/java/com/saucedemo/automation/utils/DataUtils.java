package com.saucedemo.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Utility class for data handling
 */
public class DataUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);
    private static final Random random = new Random();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Generate a random string of specified length
     *
     * @param length Length of the string
     * @return Random string
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * Generate a random email address
     *
     * @return Random email address
     */
    public static String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "@example.com";
    }
    
    /**
     * Generate a random UUID
     *
     * @return Random UUID as string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Get current timestamp as formatted string
     *
     * @param format Date format pattern
     * @return Formatted timestamp
     */
    public static String getCurrentTimestamp(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());
    }
    
    /**
     * Generate a random number within a range
     *
     * @param min Minimum value (inclusive)
     * @param max Maximum value (exclusive)
     * @return Random number
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }
    
    /**
     * Read JSON file and convert to specified class
     *
     * @param filePath  Path to JSON file
     * @param valueType Class to convert to
     * @param <T>       Type parameter
     * @return Object of specified class
     */
    public static <T> T readJsonFile(String filePath, Class<T> valueType) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            return null;
        }
    }
}