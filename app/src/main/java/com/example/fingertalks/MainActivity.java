package com.example.fingertalks;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Objects;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements TranslateFragment.OnSpeechRequestedListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private String languageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Add the dictionary fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragmen = new DictionaryFragment();
        fragmentTransaction.add(R.id.fragment_container, fragmen);
        fragmentTransaction.commit();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if (item.getItemId() == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_translate) {
                TranslateFragment translateFragment = new TranslateFragment();
                translateFragment.setOnSpeechRequestedListener(this);
                fragment = translateFragment;
            } else if (item.getItemId() == R.id.nav_dictionary) {
                fragment = new DictionaryFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    @Override
    public void onSpeechRequested(String languageCode) {
        startSpeechRecognition(languageCode);
        this.languageCode=languageCode;
    }

    private void startSpeechRecognition(String languageCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in " + languageCode);


        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String recognizedText = Objects.requireNonNull(result).get(0);

            // Check the detected language from the speech recognition result

            if ("en".equals(languageCode)) {
                // If the language is English, no need to translate
                updateTranslateFragment(recognizedText);
            } else {
                // Otherwise, translate the text
                new Thread(() -> {
                    String translatedText = translateToEnglish(recognizedText, languageCode);
                    runOnUiThread(() -> updateTranslateFragment(translatedText));
                }).start();
            }
        }
    }

    private String translateToEnglish(String text, String sourceLanguage) {
        try {
            // MyMemory API translation URL
            String apiUrl = "https://api.mymemory.translated.net/get?q=" +
                    text.replace(" ", "%20") + "&langpair=" + sourceLanguage + "|en";
            JSONObject jsonObject = getJsonObject(apiUrl);
            String translatedText = jsonObject.getJSONObject("responseData").getString("translatedText");
            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private static JSONObject getJsonObject(String apiUrl) throws IOException, JSONException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the translation response
        JSONObject jsonObject = new JSONObject(response.toString());
        return jsonObject;
    }

    private void updateTranslateFragment(String text) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof TranslateFragment) {
            ((TranslateFragment) currentFragment).updateInputText(text);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
