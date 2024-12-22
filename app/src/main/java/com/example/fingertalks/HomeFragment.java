package com.example.fingertalks;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the onClick listener for the "Start Translating" button
        view.findViewById(R.id.start_translate_button).setOnClickListener(v -> {
            // Create an instance of TranslateFragment
            TranslateFragment translateFragment = new TranslateFragment();
            // Get the FragmentManager and start a transaction
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the current fragment with the TranslateFragment
            fragmentTransaction.replace(R.id.fragment_container, translateFragment);
            // Add the transaction to the back stack so the user can navigate back
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
        });
    }
}
