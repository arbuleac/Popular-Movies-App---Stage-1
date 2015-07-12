package com.arbuleac.movieapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.arbuleac.movieapp.ui.fragment.MovieDetailFragment;
import com.arbuleac.movieapp.R;
import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.ui.fragment.MovieGridFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MovieGridActivity extends AppCompatActivity
        implements MovieGridFragment.Callbacks {

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (twoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            startActivity(detailIntent);
        }
    }
}
