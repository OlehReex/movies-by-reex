package com.moviesbyreex;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.moviesbyreex.data.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewBigPoster = findViewById((R.id.imageViewBigPoster));
        textViewTitle = findViewById(R.id.textViewMovieTitle);
        textViewRating = findViewById((R.id.textViewRating));
        textViewReleaseDate = findViewById(R.id.textViewRelease);
        textViewOverview = findViewById(R.id.textViewOverview);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            movie = (Movie) intent.getSerializableExtra("movie");
        } else {
            finish();
        }

        if (movie != null) {
            Glide.with(this)
                    .load(movie.getBigPosterPath())
                    .into(imageViewBigPoster);
            textViewTitle.setText(movie.getTitle());
            textViewRating.setText(Double.toString(movie.getVoteAverage()));
            textViewReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
            textViewOverview.setText(movie.getOverview());
        }
    }
}
