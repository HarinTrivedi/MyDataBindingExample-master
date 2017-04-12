package com.hlabexamples.databindingexample.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.hlabexamples.databindingexample.App;

public class CommonBindingUtils {

    @BindingAdapter(value = {"drawableId", "placeholder"}, requireAll = false)
    public static void setDrawable(ImageView imageView, int drawableId, Drawable placeholder) {
        BitmapTypeRequest<Integer> target = Glide.with(App.getInstance().getApplicationContext()).load(drawableId != 0 ? drawableId : null).asBitmap();
        if (placeholder != null) {
            target.placeholder(placeholder).error(placeholder);
        }
        target.into(imageView);
    }

    @BindingAdapter(value = {"imageUrl", "placeholder"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, String imageUrl, Drawable placeholder) {
        BitmapTypeRequest<String> target = Glide.with(App.getInstance().getApplicationContext()).load(imageUrl != null ? imageUrl : null).asBitmap();
        if (placeholder != null) {
            target.placeholder(placeholder).error(placeholder);
        }
        target.into(imageView);
    }
}
