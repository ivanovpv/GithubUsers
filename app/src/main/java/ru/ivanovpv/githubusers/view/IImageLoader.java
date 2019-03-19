package ru.ivanovpv.githubusers.view;

import android.widget.ImageView;

public interface IImageLoader {
    void initLoader();
    void loadImage(String url, ImageView imageView);
}
