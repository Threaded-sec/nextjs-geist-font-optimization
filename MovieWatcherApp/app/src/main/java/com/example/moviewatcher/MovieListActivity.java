package com.example.moviewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviewatcher.adapters.MovieAdapter;
import com.example.moviewatcher.managers.CreditsManager;
import com.example.moviewatcher.models.Episode;
import com.example.moviewatcher.models.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private RecyclerView moviesRecyclerView;
    private TextView creditsTextView;
    private View loadingProgressBar;
    private MovieAdapter movieAdapter;
    private CreditsManager creditsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        creditsTextView = findViewById(R.id.creditsTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Initialize managers
        creditsManager = CreditsManager.getInstance(this);

        // Setup RecyclerView
        movieAdapter = new MovieAdapter(this);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moviesRecyclerView.setAdapter(movieAdapter);

        // Load movies
        loadMovies();
        updateCreditsDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCreditsDisplay();
    }

    private void updateCreditsDisplay() {
        creditsTextView.setText(getString(R.string.available_credits, creditsManager.getCredits()));
    }

    private void loadMovies() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        
        // In a real app, this would be loaded from a server
        // For demo purposes, we're creating sample data
        List<Movie> movies = createSampleMovies();
        
        movieAdapter.setMovies(movies);
        loadingProgressBar.setVisibility(View.GONE);
    }

    private List<Movie> createSampleMovies() {
        List<Movie> movies = new ArrayList<>();

        // Sample movie 1
        List<Episode> episodes1 = Arrays.asList(
            new Episode("1", "Pilot", 1, "https://example.com/video1.mp4",
                    "https://images.pexels.com/photos/1001682/pexels-photo-1001682.jpeg",
                    "45", 2),
            new Episode("2", "The Beginning", 2, "https://example.com/video2.mp4",
                    "https://images.pexels.com/photos/1001683/pexels-photo-1001683.jpeg",
                    "42", 2)
        );
        movies.add(new Movie("1", "The Adventure Begins", 
                "An epic journey through unknown territories",
                "https://images.pexels.com/photos/1001682/pexels-photo-1001682.jpeg",
                "120", episodes1, 5));

        // Sample movie 2
        List<Episode> episodes2 = Arrays.asList(
            new Episode("3", "New World", 1, "https://example.com/video3.mp4",
                    "https://images.pexels.com/photos/1001684/pexels-photo-1001684.jpeg",
                    "48", 3)
        );
        movies.add(new Movie("2", "Space Explorers", 
                "Journey to the stars and beyond",
                "https://images.pexels.com/photos/1001684/pexels-photo-1001684.jpeg",
                "95", episodes2, 3));

        // Add more sample movies as needed
        return movies;
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MoviePlayerActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
