package com.example.moviewatcher;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviewatcher.adapters.EpisodeAdapter;
import com.example.moviewatcher.managers.AdManager;
import com.example.moviewatcher.managers.CreditsManager;
import com.example.moviewatcher.models.Episode;
import com.example.moviewatcher.models.Movie;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class MoviePlayerActivity extends AppCompatActivity implements EpisodeAdapter.OnEpisodeClickListener {
    private PlayerView playerView;
    private ExoPlayer player;
    private TextView titleTextView;
    private TextView episodeTextView;
    private TextView creditsTextView;
    private View loadingProgressBar;
    private RecyclerView episodesRecyclerView;
    private EpisodeAdapter episodeAdapter;
    
    private Movie movie;
    private Episode currentEpisode;
    private CreditsManager creditsManager;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        // Get movie from intent
        movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie == null || movie.getEpisodes().isEmpty()) {
            finish();
            return;
        }

        // Initialize managers
        creditsManager = CreditsManager.getInstance(this);
        adManager = AdManager.getInstance();

        // Initialize views
        initializeViews();
        setupEpisodesRecyclerView();
        initializePlayer();

        // Start with first episode
        playEpisode(movie.getEpisodes().get(0));
        updateCreditsDisplay();
    }

    private void initializeViews() {
        playerView = findViewById(R.id.playerView);
        titleTextView = findViewById(R.id.titleTextView);
        episodeTextView = findViewById(R.id.episodeTextView);
        creditsTextView = findViewById(R.id.creditsTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        episodesRecyclerView = findViewById(R.id.episodesRecyclerView);

        titleTextView.setText(movie.getTitle());
    }

    private void setupEpisodesRecyclerView() {
        episodeAdapter = new EpisodeAdapter(this);
        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        episodesRecyclerView.setAdapter(episodeAdapter);
        episodeAdapter.setEpisodes(movie.getEpisodes());
    }

    private void initializePlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    onEpisodeCompleted();
                }
            }
        });
    }

    private void playEpisode(Episode episode) {
        if (!creditsManager.hasEnoughCredits(episode.getCreditCost())) {
            showWatchAdDialog(episode);
            return;
        }

        currentEpisode = episode;
        episodeTextView.setText(getString(R.string.episode, episode.getEpisodeNumber()));
        
        // Deduct credits
        creditsManager.deductCredits(episode.getCreditCost());
        updateCreditsDisplay();

        // Prepare and play video
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(episode.getVideoUrl()));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    private void showWatchAdDialog(Episode episode) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.watch_ad_title)
                .setMessage(getString(R.string.need_more_credits, 
                        creditsManager.getCreditsNeeded(episode.getCreditCost())))
                .setPositiveButton(R.string.watch_ad, (dialog, which) -> showRewardedAd(episode))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showRewardedAd(Episode episode) {
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
                    if (creditsManager.hasEnoughCredits(episode.getCreditCost())) {
                        playEpisode(episode);
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(String error) {
                runOnUiThread(() -> 
                    Toast.makeText(MoviePlayerActivity.this, 
                        R.string.error_loading_ad, 
                        Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onAdDismissed() {
                adManager.preloadAd(MoviePlayerActivity.this);
            }
        });
    }

    private void updateCreditsDisplay() {
        creditsTextView.setText(getString(R.string.available_credits, creditsManager.getCredits()));
    }

    private void onEpisodeCompleted() {
        currentEpisode.setWatched(true);
        episodeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEpisodeClick(Episode episode) {
        playEpisode(episode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
