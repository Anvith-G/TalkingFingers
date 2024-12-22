package com.example.fingertalks;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DictionaryFragment extends Fragment {

    private EditText searchWord;
    private Button searchButton;
    private VideoView signVideo;
    private TextView noResultText;
    private Map<String, String> dictionary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        searchWord = view.findViewById(R.id.search_word);
        searchButton = view.findViewById(R.id.search_button);
        signVideo = view.findViewById(R.id.sign_video);
        noResultText = view.findViewById(R.id.no_result_text);

        dictionary = loadDictionaryFromCSV();

        // Set up MediaController
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(signVideo);
        signVideo.setMediaController(mediaController);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = searchWord.getText().toString().trim();
                if (!word.isEmpty()) {
                    searchWordInDictionary(word);
                }
            }
        });

        // Enable looping
        signVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        return view;
    }

    private Map<String, String> loadDictionaryFromCSV() {
        Map<String, String> dictionary = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().getAssets().open("updated_dictionary.csv")));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String word = nextLine[0].trim().toLowerCase();
                String videoUrl = nextLine[1].trim();
                dictionary.put(word, videoUrl);
                Log.d("DictionaryFragment", "Loaded word: " + word + ", URL: " + videoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DictionaryFragment", "Error loading CSV file", e);
        }
        return dictionary;
    }

    private void searchWordInDictionary(String word) {
        String videoUrl = dictionary.get(word.toLowerCase());
        Log.d("DictionaryFragment", "Searching for word: " + word + ", Found URL: " + videoUrl);

        if (videoUrl != null) {
            signVideo.setVisibility(View.VISIBLE);
            noResultText.setVisibility(View.GONE);
            signVideo.setVideoURI(Uri.parse(videoUrl));
            signVideo.start();
        } else {
            signVideo.setVisibility(View.GONE);
            noResultText.setVisibility(View.VISIBLE);
        }
    }
}
