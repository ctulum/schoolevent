package com.example.schoolevents.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * {@link ImageEngine} implementation using Glide.
 */

public class Glide4Engine implements ImageEngine {

    private static final String TAG = "Glide4Engine";
    private ImageLoader imageLoader;

    public Glide4Engine(Context context){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(context);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        imageLoader.displayImage(uri.toString(), imageView);
        /*Picasso.get()
                .load(uri)
                .rotate(0)
                .centerCrop()
                .placeholder(placeholder)
                .resize(resize, resize)
                .into(imageView);*/
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
                                 Uri uri) {
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        imageLoader.displayImage(uri.toString(), imageView);
        /*Picasso.get()
                .load(uri)
                .resize(resizeX, resizeY)
                .priority(Picasso.Priority.HIGH)
                .rotate(0)
                .centerInside()
                .into(imageView);*/
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }

}
