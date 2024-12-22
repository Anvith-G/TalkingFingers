package com.example.fingertalks;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ResourceHelper {
    private static Context context;

    // Maps for letters and words
    private static Map<String, Integer> letterMap = new HashMap<>();
    private static Map<String, Integer> wordMap = new HashMap<>();

    // Initialize the ResourceHelper with application context
    public static void initialize(Context appContext) {
        context = appContext;
        loadResources();
    }

    // Loads both letter and word resources into the maps
    private static void loadResources() {
        // Map letters a-z
        for (char letter = 'a'; letter <= 'z'; letter++) {
            String resourceName = letter + "_small"; // Example: a_small, b_small
            int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                letterMap.put(String.valueOf(letter), resourceId);
            }
        }

        // Map words based on the word list
        String[] wordFiles = {"village","address",
                "temple",
                "ahemdabad",
                "thursday",
                "all",
                "tomato",
                "august",
                "tuesday",
                "banana",
                "usa",
                "cat",
                "village",
                "christmas",
                "wednesday",
                "church", "cilinic",
                "dasara",
                "december",
                "grapes",
                "hello",
                "job",
                "july",
                "june",
                "karnataka",
                "kerala",
                "mango",
                "may",
                "mile",
                "mumbai",
                "nagpur",
                "pakistan",
                "saturday",
                "shop",


        };  // Add more words here as needed
        for (String word : wordFiles) {
            int resourceId = context.getResources().getIdentifier(word, "drawable", context.getPackageName());
            if (resourceId != 0) {
                wordMap.put(word, resourceId);
            }
        }
    }

    // Get the resource ID for a letter
    public static int getLetterResourceId(String letter) {
        return letterMap.getOrDefault(letter, 0);  // Return 0 if not found
    }

    // Get the resource ID for a word
    public static int getWordResourceId(String word) {
        return wordMap.getOrDefault(word, 0);  // Return 0 if not found
    }
}
