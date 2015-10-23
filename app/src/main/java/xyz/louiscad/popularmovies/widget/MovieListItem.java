package xyz.louiscad.popularmovies.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import trikita.log.Log;
import xyz.louiscad.popularmovies.R;
import xyz.louiscad.popularmovies.model.Movie;
import xyz.louiscad.popularmovies.util.ImageUtil;
import xyz.louiscad.popularmovies.util.recyclerview.ViewWrapper;

/**
 * Created by Louis Cognault on 11/10/15.
 */
@EViewGroup(R.layout.list_item_movie)
public class MovieListItem extends FrameLayout implements ViewWrapper.Binder<Movie> {

    @ViewById
    TextView titleTextView;

    @ViewById
    SimpleDraweeView posterImage;

    @ViewById
    View footerBackground;

    public MovieListItem(Context context) {
        super(context);
    }

    @Override
    public void bind(final Movie movie) {
        titleTextView.setText(movie.title);
        Uri posterUri = ImageUtil.getPosterUri(movie.poster_path);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (movie.posterPalette == null) {
            DataSource<CloseableReference<CloseableImage>> dataSource
                    = imagePipeline.fetchImageFromBitmapCache(ImageRequest.fromUri(posterUri), null);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    if (bitmap != null) {
                        movie.posterPalette = new Palette.Builder(bitmap).generate();
                        setFooterColor(movie.posterPalette);
                        Log.i("new bitmap received");
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    Log.i("onFailureImpl");
                }
            }, new DefaultExecutorSupplier(1).forBackgroundTasks());
        } else {
            setFooterColor(movie.posterPalette);
        }
        posterImage.setImageURI(posterUri);
    }

    @UiThread
    void setFooterColor(Palette palette) {
        int color = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
        footerBackground.setBackgroundColor(color);
    }
}