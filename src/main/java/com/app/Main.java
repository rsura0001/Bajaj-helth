package com.app;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid CLI Startup: try->  java -jar mybajaj-0.0.1-SNAPSHOT-jar-with-dependencies.jar 240350000046 \"C:\\Users\\91788\\Desktop\\test.json\"\r\n"
            		+ "");
            return;
        }

        String prnNumber = args[0];
        String jsonFilePath = args[1];

        String destinationValue = findDestinationValue(jsonFilePath);
        if (destinationValue == null) {
            System.out.println("Key 'destination' not found in the JSON file.");
            return;
        }
	
        String randomString = generateRandomString(8);
        String concatenatedString = prnNumber + destinationValue + randomString;
        String md5Hash = generateMD5Hash(concatenatedString);

        System.out.println(md5Hash + ";" + randomString);
    }

    private static String findDestinationValue(String jsonFilePath) {
        try {
        	FileReader reader = new FileReader(jsonFilePath);
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            reader=null;
            return findValueForKey(jsonObject, "destination");
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            return null;
        }finally {
        }
    }

    private static String findValueForKey(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getString(key);
        }

        for (String k : jsonObject.keySet()) {
            Object value = jsonObject.get(k);
            if (value instanceof JSONObject) {
                String result = findValueForKey((JSONObject) value, key);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    private static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating MD5 hash", e);
        }
    }
}
	