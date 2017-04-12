package com.hlabexamples.databindingexample.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlabexamples.databindingexample.App;
import com.hlabexamples.databindingexample.R;
import com.hlabexamples.databindingexample.databinding.RowItemAttractionBinding;
import com.hlabexamples.databindingexample.util.LoadingViewHolder;

import java.util.List;

/**
 * Created in BindingConstraintMVP-Demo on 11/01/17.
 */

class BrowseAttractionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AttractionModel> items;
    private LayoutInflater inflater;

    private BrowseAttractionFragment fragment;
    private ItemType itemType;

    BrowseAttractionAdapter(BrowseAttractionFragment fragment, List<AttractionModel> items, ItemType itemType) {
        this.itemType = itemType;
        this.fragment = fragment;
        this.items = items;
        inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new LoadingViewHolder(inflater.inflate(R.layout.layout_loading_item, parent, false));
        } else {
            RowItemAttractionBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_item_attraction, parent, false);
            return new BrowseItemHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            AttractionModel model = items.get(position);
            model.setFavourite(App.getInstance().getFavItems().contains(model));
            ((BrowseItemHolder) holder).bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 1 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (items.get(position) == null) {
            return 0;
        } else {
            return 1;
        }
    }

    private class BrowseItemHolder extends RecyclerView.ViewHolder implements BrowseItemPresenter {

        private RowItemAttractionBinding binding;

        BrowseItemHolder(RowItemAttractionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AttractionModel item) {
            binding.setPresenter(this);
            binding.setData(item);
            ViewCompat.setTransitionName(binding.imgMain, "image_" + getAdapterPosition());
        }

        @Override
        public void onClick() {
            fragment.showDetail(items.get(getAdapterPosition()), binding.imgMain);
        }

        @Override
        public void onMenuClick(View view) {
            fragment.showMenu(view, binding.getData(), itemType);
        }
    }
}
