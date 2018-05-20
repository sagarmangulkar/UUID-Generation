package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        while (true) {
            ArrayList<String> words = new ArrayList<>();
            words.add(getCamelCaseRandomWordFromCrayola());
            words.add(getCamelCaseRandomWordFromOccupations());
            words.add(getCamelCaseRandomWordFromAdjs());
            String uniqueString = words.get(0) + words.get(1) + words.get(2);
            System.out.println(uniqueString);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            bufferedReader.readLine();
        }
    }

    private static String getCamelCaseRandomWordFromCrayola() throws Exception{
        JSONObject jsonObject = parseJSONFile("crayola.json");
        JSONArray colors = (JSONArray)jsonObject.get("colors");
        int randomIndex = generateRandomIndex(colors.size());
        String crayolaWord = (String)((JSONObject)colors.get(randomIndex)).get("color");
        return camelCase(crayolaWord);
    }

    private static String getCamelCaseRandomWordFromOccupations() throws Exception{
        JSONObject jsonObject = parseJSONFile("occupations.json");
        JSONArray occupations = (JSONArray)jsonObject.get("occupations");
        int randomIndex = generateRandomIndex(occupations.size());
        String occupationsWord = (String) occupations.get(randomIndex);
        return camelCase(occupationsWord);
    }

    private static String getCamelCaseRandomWordFromAdjs() throws Exception{
        JSONObject jsonObject = parseJSONFile("adjs.json");
        JSONArray adjectives = (JSONArray) jsonObject.get("adjs");
        int randomIndex = generateRandomIndex(adjectives.size());
        String adjsWord = (String)adjectives.get(randomIndex);
        return camelCase(adjsWord);
    }

    private static String camelCase(String words) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words.split(" ")) {
            stringBuilder.append(Character.toUpperCase(word.charAt(0)));
            if(word.length() > 1){
                stringBuilder.append(word.substring(1, word.length()).toLowerCase());
            }
        }
        return stringBuilder.toString();
    }

    private static int generateRandomIndex(int size) {
        Random random = new Random();
        int randomIndex = random.nextInt(size);      //give random value between 0 and size
        return randomIndex;
    }

    public static Integer[] sampleRandomNumbersWithoutRepetition(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }

    private static JSONObject parseJSONFile(String jsonFile) throws Exception{
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(jsonFile));
        return (JSONObject) object;
    }
}
