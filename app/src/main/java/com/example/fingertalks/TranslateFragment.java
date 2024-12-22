package com.example.fingertalks;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TranslateFragment extends Fragment {

    private static final String TAG = "TranslateFragment";
    private EditText inputText;
    private LinearLayout outputLayout;
    private Spinner languageSpinner;
    private HashMap<String, String> LANGUAGE_MAP;
    private OnSpeechRequestedListener speechListener;

    public interface OnSpeechRequestedListener {
        void onSpeechRequested(String languageCode);
    }

    public TranslateFragment() {
        // Default empty constructor required for fragments
    }

    public void setOnSpeechRequestedListener(OnSpeechRequestedListener listener) {
        this.speechListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        ResourceHelper.initialize(requireContext()); // Initialize resource mapping

        // Bind views
        inputText = view.findViewById(R.id.inputText);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        Button translateButton = view.findViewById(R.id.translateButton);
        Button talkButton = view.findViewById(R.id.talkButton);
        outputLayout = view.findViewById(R.id.outputLayout);

        // Initialize language map and spinner
        initializeLanguageMap();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(LANGUAGE_MAP.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set click listeners
        translateButton.setOnClickListener(v -> displayImages());
        talkButton.setOnClickListener(v -> {
            if (speechListener != null) {
                String selectedLanguage = languageSpinner.getSelectedItem().toString();
                String languageCode = LANGUAGE_MAP.get(selectedLanguage);
                speechListener.onSpeechRequested(languageCode);
            } else {
                Toast.makeText(getContext(), "Speech listener not set", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initializeLanguageMap() {
        LANGUAGE_MAP = new HashMap<>();
        LANGUAGE_MAP.put("English", "en");
        LANGUAGE_MAP.put("Hindi", "hi");
        LANGUAGE_MAP.put("Bengali", "bn");
        LANGUAGE_MAP.put("Kannada", "kn");
        LANGUAGE_MAP.put("Tamil", "ta");
        LANGUAGE_MAP.put("Telugu", "te");
        LANGUAGE_MAP.put("Malayalam", "ml");

        // Add more languages as needed
    }

    private void displayImages() {
        outputLayout.removeAllViews();
        String text = inputText.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(getContext(), "Please enter text", Toast.LENGTH_SHORT).show();
            return;
        }

        // Predict images based on input text
        predictSignImages(text);
    }

    private void predictSignImages(String text) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            Log.d(TAG, "Checking word: " + word);
            displayWord(word);
        }
    }

    private void displayWord(String word) {
        // Create a HorizontalScrollView
        HorizontalScrollView scrollView = new HorizontalScrollView(getContext());
        scrollView.setHorizontalScrollBarEnabled(true);

        // Create a LinearLayout to hold the images
        LinearLayout horizontalLayout = new LinearLayout(getContext());
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);

        for (char letter : word.toCharArray()) {
            int letterResourceId = ResourceHelper.getLetterResourceId(String.valueOf(letter).toLowerCase());
            if (letterResourceId != 0) {
                addImageToLayout(horizontalLayout, letterResourceId);
            } else {
                Log.w(TAG, "Image for letter '" + letter + "' not found.");
            }
        }

        // Add the horizontal layout to the scroll view
        scrollView.addView(horizontalLayout);

        // Add the scroll view to the output layout
        outputLayout.addView(scrollView);
    }


    private void addImageToLayout(LinearLayout layout, int resourceId) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                dpToPx(120),  // Width in dp
                dpToPx(100)   // Height in dp
        );
        imageParams.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(imageParams);

        // Load the image using Glide
        Glide.with(this)
                .load(resourceId)
                .into(imageView);

        layout.addView(imageView);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void updateInputText(String recognizedText) {
        if (inputText != null) {
            inputText.setText(recognizedText); // Update the EditText with recognized speech
        }
    }
}
