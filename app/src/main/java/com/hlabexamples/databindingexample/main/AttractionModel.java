package com.hlabexamples.databindingexample.main;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.hlabexamples.databindingexample.BR;

/**
 * BaseObservable exmaple
 */
public class AttractionModel extends BaseObservable implements Parcelable {
    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private int imgDrawableId = 0;
    private boolean isFavourite;

    public AttractionModel(int itemId, String itemTitle, String itemDescription, int imgDrawableId) {
        this.itemDescription = itemDescription;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.imgDrawableId = imgDrawableId;
    }

    private AttractionModel(Parcel in) {
        itemId = in.readInt();
        itemTitle = in.readString();
        itemDescription = in.readString();
        imgDrawableId = in.readInt();
        isFavourite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(itemTitle);
        dest.writeString(itemDescription);
        dest.writeInt(imgDrawableId);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttractionModel> CREATOR = new Creator<AttractionModel>() {
        @Override
        public AttractionModel createFromParcel(Parcel in) {
            return new AttractionModel(in);
        }

        @Override
        public AttractionModel[] newArray(int size) {
            return new AttractionModel[size];
        }
    };

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getImgDrawableId() {
        return imgDrawableId;
    }

    public void setImgDrawableId(int imgDrawableId) {
        this.imgDrawableId = imgDrawableId;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AttractionModel) && ((AttractionModel) obj).getItemId() == itemId;
    }

    /* --- Bindable example --- */

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
        notifyPropertyChanged(BR.favouriteVisibility);
    }

    @Bindable
    public int getFavouriteVisibility() {
        return isFavourite() ? View.VISIBLE : View.GONE;
    }

}
