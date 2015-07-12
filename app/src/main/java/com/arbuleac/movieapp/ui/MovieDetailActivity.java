package com.arbuleac.movieapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.ui.fragment.MovieDetailFragment;
import com.arbuleac.movieapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Movie movie = getIntent().getParcelableExtra(MovieDetailFragment.ARG_MOVIE);
            arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
            getSupportActionBar().setTitle(movie.getOriginalTitle());
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MovieGridActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
