package xyz.louiscad.popularmovies.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import xyz.louiscad.popularmovies.model.Movie;
import xyz.louiscad.popularmovies.util.recyclerview.RecyclerViewAdapterBase;
import xyz.louiscad.popularmovies.widget.MovieListItem;
import xyz.louiscad.popularmovies.widget.MovieListItem_;

/**
 * Created by Louis Cognault on 11/10/15.
 */
public class MovieItemAdapter extends RecyclerViewAdapterBase<Movie, MovieListItem> {

    private List<Movie> mMovieList;

    public void onDataUpdated(List<Movie> data) {
        mMovieList = data;
        notifyDataSetChanged();
    }

    @Override
    protected MovieListItem onCreateItemView(ViewGroup parent, int viewType) {
        return MovieListItem_.build(parent.getContext());
    }

    @Override
    protected Movie getData(int position) {
        return mMovieList.get(position);
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null ? 0 : mMovieList.size());
    }
}