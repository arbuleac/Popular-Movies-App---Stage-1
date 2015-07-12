package com.arbuleac.movieapp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arbuleac.movieapp.R;
import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.model.response.MovieResult;
import com.arbuleac.movieapp.sync.ApiHelper;
import com.arbuleac.movieapp.ui.MovieGridActivity;
import com.arbuleac.movieapp.ui.adapter.MovieAdapter;
import com.arbuleac.movieapp.ui.widget.EndlessRecyclerOnScrollListener;
import com.arbuleac.movieapp.util.Constants;
import com.arbuleac.movieapp.util.ContentManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @since 7/12/15.
 */
public class MovieGridFragment extends Fragment {

    private static final String LIST_STATE = "list_state";
    private static final String DATA = "data";
    @Bind(R.id.movie_grid_rv)
    RecyclerView movieGridRv;
    private int currentPage = 0;

    List<Movie> movies = new ArrayList<>();

    private View.OnClickListener retryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
        }
    };
    private MovieAdapter moviesAdapter;
    private GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        movieGridRv.setLayoutManager(gridLayoutManager);
        movieGridRv.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                MovieGridFragment.this.currentPage = currentPage;
                loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
            }
        });
        moviesAdapter = new MovieAdapter(movies);
        movieGridRv.setAdapter(moviesAdapter);
        moviesAdapter.setCallbacks((MovieGridActivity) getActivity());
        reloadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE, listState);
        outState.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) movies);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String currentSort = ContentManager.getInstance(getActivity()).getSortOrder();
        switch (currentSort) {
            case Constants.SORT_BY_POPULARITY:
                menu.findItem(R.id.action_sort_popularity).setChecked(true);
                break;
            case Constants.SORT_BY_HIGHEST_RATED:
                menu.findItem(R.id.action_sort_rating).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String currentSort = ContentManager.getInstance(getActivity()).getSortOrder();
        String selectedSort = null;
        switch (item.getItemId()) {
            case R.id.action_sort_popularity:
                selectedSort = Constants.SORT_BY_POPULARITY;
                break;
            case R.id.action_sort_rating:
                selectedSort = Constants.SORT_BY_HIGHEST_RATED;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (!currentSort.equals(selectedSort)) {
            ContentManager.getInstance(getActivity()).setSortOrder(selectedSort);
            reloadData();
        }
        item.setChecked(true);
        return true;
    }

    private void reloadData() {
        movies.clear();
        currentPage = 1;
        loadMovies(currentPage, ContentManager.getInstance(getActivity()).getSortOrder());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadMovies(int page, String sortBy) {
        ApiHelper.getApi().discoverMovie(page, sortBy, new Callback<MovieResult>() {
            @Override
            public void success(MovieResult movieResult, Response response) {
                movies.addAll(movieResult.getResults());
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                //Such a cool widget and such poor api/widget design... why can not stay longer? why can not be forever on screen?
                Snackbar.make(getView(), getString(R.string.movies_load_error_text), Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW).setAction(getString(R.string.retry), retryClickListener).show();
            }
        });
    }

    public interface Callbacks {
        void onItemSelected(Movie movie);
    }
}
