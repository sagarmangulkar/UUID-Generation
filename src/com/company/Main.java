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
import java.util.HashSet;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        HashSet<String> uuid = new HashSet<>();
        //while (true) {
        JSONArray colors = parseJSONFile("crayola.json", "colors");
        JSONArray occupations = parseJSONFile("occupations.json", "occupations");
        JSONArray adjectives = parseJSONFile("adjs.json", "adjs");
        int totalCombinations = colors.size() * occupations.size() * adjectives.size();
        Integer[] randomNumbers = GetRandomNumbersWithoutRepetition(totalCombinations);
        System.out.println("totalCombinations: " + totalCombinations);
            for (int i = 0; i < totalCombinations; i++) {
                ArrayList<String> words = new ArrayList<>();
                int crayolaRandomIndex = randomNumbers[i]%colors.size();
                words.add(getCamelCaseRandomWordFromCrayola(colors, crayolaRandomIndex));
                int occupationRandomIndex = (randomNumbers[i]/colors.size())%occupations.size();
                words.add(getCamelCaseRandomWordFromOccupations(occupations, occupationRandomIndex));
                int adjectiveRandomIndex = (randomNumbers[i]/(colors.size()*occupations.size()))%occupations.size();
                words.add(getCamelCaseRandomWordFromAdjs(adjectives, adjectiveRandomIndex));
                String uniqueString = words.get(0) + words.get(1) + words.get(2);
                System.out.println(i + ". " + uniqueString);
                if(!uuid.contains(uniqueString)) {
                    uuid.add(uniqueString);
                }
                else if (uuid.contains(uniqueString)){
                    System.out.println("--------------Repeated UUID------------");
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                bufferedReader.readLine();
            }
        //}
    }

    private static Integer[] GetRandomNumbersWithoutRepetition(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        return arr;
    }

    private static String getCamelCaseRandomWordFromCrayola(JSONArray colors, int randomIndex) throws Exception{
        String crayolaWord = (String)((JSONObject)colors.get(randomIndex)).get("color");
        return camelCase(crayolaWord);
    }

    private static String getCamelCaseRandomWordFromOccupations(JSONArray occupations, int randomIndex) throws Exception{
        String occupationsWord = (String) occupations.get(randomIndex);
        return camelCase(occupationsWord);
    }

    private static String getCamelCaseRandomWordFromAdjs(JSONArray adjectives, int randomIndex) throws Exception{
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

    private static JSONArray parseJSONFile(String jsonFile, String attribute) throws Exception{
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(jsonFile));
        return (JSONArray)jsonObject.get(attribute);
    }
}
