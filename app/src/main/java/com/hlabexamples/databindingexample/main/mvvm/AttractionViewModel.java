package com.hlabexamples.databindingexample.main.mvvm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.hlabexamples.databindingexample.BR;

/**
 * Created in BindingConstraintMVP-Demo on 13/01/17.
 */

public class AttractionViewModel extends BaseObservable implements OnFavoriteChangedListener {

    private AttractionModel attractionModel;

    public AttractionViewModel(AttractionModel mAttractionModel) {
        this.attractionModel = mAttractionModel;
    }

    @Override
    public void onFavoriteChange(boolean isFavourite) {
        attractionModel.setFavourite(isFavourite);
        notifyPropertyChanged(BR.favouriteVisibility);
    }

    @Bindable
    public int getFavouriteVisibility() {
        return attractionModel.isFavourite() ? View.VISIBLE : View.GONE;
    }

    public AttractionModel getAttractionModel() {
        return attractionModel;
    }
}
