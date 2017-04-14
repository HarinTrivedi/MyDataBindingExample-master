package com.hlabexamples.databindingexample;

import android.app.Application;

import com.hlabexamples.databindingexample.main.mvvm.AttractionModel;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private static App instance;
    private List<AttractionModel> favItems;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        favItems = new ArrayList<>();
    }

    public void addToFav(AttractionModel model) {
        if (!favItems.contains(model))
            favItems.add(model);
    }

    public void removeFromFav(AttractionModel model) {
        favItems.remove(favItems.indexOf(model));
    }

    public List<AttractionModel> getFavItems() {
        return favItems;
    }
}
