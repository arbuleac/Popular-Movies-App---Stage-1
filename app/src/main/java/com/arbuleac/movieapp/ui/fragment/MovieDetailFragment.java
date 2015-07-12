package com.arbuleac.movieapp.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arbuleac.movieapp.R;
import com.arbuleac.movieapp.model.Movie;
import com.arbuleac.movieapp.util.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {
    public static final String ARG_MOVIE = "movie";

    @Bind(R.id.cover_iv)
    ImageView coverIv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.date_tv)
    TextView releaseDateTv;
    @Bind(R.id.rating_tv)
    TextView ratingTv;
    @Bind(R.id.overview_tv)
    TextView overviewTv;

    private Movie movie;
    private Palette colorPalette;
    private Handler handler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity())
                .load(Constants.STATIC_IMAGE_ENDPOINT + Constants.DETAILS_IMAGE_SIZE + movie.getPosterPath())
                .fit()
                .centerCrop()
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Palette.Builder builder = new Palette.Builder(source);
                        colorPalette = builder.generate();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                titleTv.setTextColor(colorPalette.getDarkVibrantColor(Color.BLACK));
                            }
                        });
                        return source;
                    }

                    @Override
                    public String key() {
                        return "palette";
                    }
                })
                .into(coverIv);


        titleTv.setText(movie.getOriginalTitle());

        Date releaseDate = movie.getReleaseDate();
        releaseDateTv.setText(dateFormat.format(releaseDate));

        SpannableStringBuilder ratingBuilder = new SpannableStringBuilder("Rating:");
        ratingBuilder.append(' ').append(String.valueOf(movie.getVoteAverage())).append("/10 (").append(String.valueOf(movie.getVoteCount())).append(')');
        ratingBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ratingTv.setText(ratingBuilder);

        if (movie.getOverview() != null) {
            SpannableStringBuilder overviewBuilder = new SpannableStringBuilder("Overview:\n\n");
            overviewBuilder.append(movie.getOverview());
            overviewBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            overviewTv.setText(overviewBuilder);
        } else {
            overviewTv.setText(R.string.empty);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
