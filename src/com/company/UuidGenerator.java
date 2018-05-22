package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class UuidGenerator {

    private JSONArray adjectives;
    private JSONArray colors;
    private JSONArray occupations;
    private int totalCombinations;
    private Integer[] randomNumbers;
    private volatile int counter = 0;

    UuidGenerator(String adjsJson, String crayolaJson, String occupationsJson) throws Exception {
        int adjectivesSize = 1, colorsSize = 1, occupationsSize = 1;
        if (adjsJson != null) {
            adjectives = parseJSONFile(adjsJson, "adjs");
            adjectivesSize = adjectives.size();
        }
        if (crayolaJson != null) {
            colors = parseJSONFile(crayolaJson, "colors");
            colorsSize = colors.size();
        }
        if (occupationsJson != null) {
            occupations = parseJSONFile(occupationsJson, "occupations");
            occupationsSize = occupations.size();
        }
        totalCombinations = adjectivesSize * colorsSize * occupationsSize;
        randomNumbers = getRandomNumbersWithoutRepetition(totalCombinations);
    }

    public static void main(String[] args) {
        UuidGenerator uuidGenerator = null;
        try {
            uuidGenerator = new UuidGenerator(args[0], args[1], args[2]);
            while(true) {
                System.out.println(uuidGenerator.getUuid());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Thank you.");
        }
    }

    private String getUuid() throws Exception{
        if(counter >= totalCombinations) {
            throw new Exception("Possible UUIDs are finished");
        }
        int adjectivesSize = adjectives.size();
        int colorsSize = colors.size();
        int occupationsSize = occupations.size();
        StringBuilder stringBuilder = new StringBuilder();

        int crayolaRandomIndex = randomNumbers[counter] % colorsSize;
        stringBuilder.append(camelCase((String)((JSONObject)colors.get(crayolaRandomIndex)).get("color")));

        int occupationsRandomIndex = (randomNumbers[counter] / colorsSize) % occupationsSize;
        stringBuilder.append(camelCase((String) occupations.get(occupationsRandomIndex)));

        int adjectiveRandomIndex = (randomNumbers[counter] / (colorsSize * occupationsSize)) % adjectivesSize;
        stringBuilder.append(camelCase((String)adjectives.get(adjectiveRandomIndex)));

        counter++;
        return stringBuilder.toString();
    }

    private Integer[] getRandomNumbersWithoutRepetition(int size) {
        Integer[] randomNumbers = new Integer[size];
        for (int i = 0; i < size; i++) {
            randomNumbers[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomNumbers));
        return randomNumbers;
    }

    private String camelCase(String words) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words.split(" ")) {
            stringBuilder.append(Character.toUpperCase(word.charAt(0)));
            if(word.length() > 1){
                stringBuilder.append(word.substring(1, word.length()).toLowerCase());
            }
        }
        return stringBuilder.toString();
    }

    private static JSONArray parseJSONFile(String jsonFile, String attribute) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(jsonFile));
        return (JSONArray)jsonObject.get(attribute);
    }

}
