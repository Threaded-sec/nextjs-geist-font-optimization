package com.example.moviewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviewatcher.managers.AdManager;
import com.example.moviewatcher.managers.CreditsManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private CreditsManager creditsManager;
    private AdManager adManager;
    private TextView creditsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize managers
        creditsManager = CreditsManager.getInstance(this);
        adManager = AdManager.getInstance();
        adManager.initialize(this);

        // Initialize views
        creditsTextView = findViewById(R.id.creditsTextView);
        MaterialButton browseMoviesButton = findViewById(R.id.browseMoviesButton);
        MaterialButton getCreditsButton = findViewById(R.id.getCreditsButton);

        // Set click listeners
        browseMoviesButton.setOnClickListener(v -> openMovieList());
        getCreditsButton.setOnClickListener(v -> showWatchAdDialog());

        // Update credits display
        updateCreditsDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCreditsDisplay();
        adManager.preloadAd(this);
    }

    private void updateCreditsDisplay() {
        creditsTextView.setText(getString(R.string.available_credits, creditsManager.getCredits()));
    }

    private void openMovieList() {
        Intent intent = new Intent(this, MovieListActivity.class);
        startActivity(intent);
    }

    private void showWatchAdDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.watch_ad_title)
                .setMessage(R.string.watch_ad_message)
                .setPositiveButton(R.string.watch_ad, (dialog, which) -> showRewardedAd())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showRewardedAd() {
        if (!adManager.isAdAvailable()) {
            Toast.makeText(this, R.string.error_loading_ad, Toast.LENGTH_SHORT).show();
            return;
        }

        adManager.showRewardedAd(this, new AdManager.AdCallback() {
            @Override
            public void onAdRewarded() {
                creditsManager.addCreditsForAd();
                runOnUiThread(() -> {
                    updateCreditsDisplay();
                    Toast.makeText(MainActivity.this, 
                        getString(R.string.credits_earned, 5), 
                        Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onAdFailedToLoad(String error) {
                runOnUiThread(() -> 
                    Toast.makeText(MainActivity.this, 
                        R.string.error_loading_ad, 
                        Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onAdDismissed() {
                // Preload the next ad
                adManager.preloadAd(MainActivity.this);
            }
        });
    }
}
