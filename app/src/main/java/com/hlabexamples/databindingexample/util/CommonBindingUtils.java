package com.hlabexamples.databindingexample.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.hlabexamples.databindingexample.App;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonBindingUtils {

    /**
     * Example 1/1
     * To show image from drawable
     *
     * @param imageView
     * @param drawableId
     * @param placeholder
     */
    @BindingAdapter(value = {"drawableId", "placeholder"}, requireAll = false)
    public static void setDrawable(ImageView imageView, int drawableId, Drawable placeholder) {
        BitmapTypeRequest<Integer> target = Glide.with(App.getInstance().getApplicationContext()).load(drawableId != 0 ? drawableId : null).asBitmap();
        if (placeholder != null) {
            target.placeholder(placeholder).error(placeholder);
        }
        target.into(imageView);
    }

    /**
     * To show image from url
     * Example 1/2
     * @param imageView
     * @param imageUrl
     * @param placeholder
     */
    @BindingAdapter(value = {"imageUrl", "placeholder"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, String imageUrl, Drawable placeholder) {
        BitmapTypeRequest<String> target = Glide.with(App.getInstance().getApplicationContext()).load(imageUrl != null ? imageUrl : null).asBitmap();
        if (placeholder != null) {
            target.placeholder(placeholder).error(placeholder);
        }
        target.into(imageView);
    }

    /**
     * Example 2
     * To show greetings with name
     *
     * @param view
     * @param text
     */
    @BindingAdapter("greetings")
    public static void setName(TextView view, String text) {
        view.setText(String.format("Welcome, %s", text));
    }

    /**
     * Example 3
     * Show age calculated from dob (ex. format : dd-MM-yyyy)
     *
     * @param textView
     * @param dob
     */
    @BindingAdapter("age")
    public static void setAge(TextView textView, String dob) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Calendar today = Calendar.getInstance();
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(format.parse(dob));

            int age = today.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR) && age > 0) {
                age--;
            }

            textView.setText(String.valueOf(age));
        } catch (Exception ignored) {
        }
    }
}
