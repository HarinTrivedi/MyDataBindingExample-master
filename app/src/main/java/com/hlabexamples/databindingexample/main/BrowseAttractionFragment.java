package com.hlabexamples.databindingexample.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hlabexamples.databindingexample.App;
import com.hlabexamples.databindingexample.R;
import com.hlabexamples.databindingexample.databinding.FragmentBrowseAttractionsBinding;
import com.hlabexamples.databindingexample.detail.DetailActivity;
import com.hlabexamples.databindingexample.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BrowseAttractionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final int PAGE_SIZE = 10;
    private final String TAG = BrowseAttractionFragment.class.getSimpleName();
    private FragmentBrowseAttractionsBinding binding;
    private BrowseAttractionAdapter adapter;
    private ItemType itemType;

    private List<AttractionModel> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse_attractions, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        checkArguments(getArguments());
        initRecyclerView();
        refreshData();
    }

    protected void checkArguments(Bundle bundle) {
        if (bundle != null && bundle.containsKey(getString(R.string.arg_type)))
            itemType = (ItemType) getArguments().getSerializable(getString(R.string.arg_type));
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.frBrowseRecyclerView.setLayoutManager(layoutManager);
        binding.frBrowseRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 0;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount >= PAGE_SIZE) {
                    Log.e(TAG, "onLoadMore: " + page);
                    fetchItems(getActivity(), itemType, page + 1);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (data != null)
            data.clear();
        displayItems(fetchItems(getActivity(), itemType, 1), itemType);
    }

    public void refreshData() {
        onRefresh();
    }

    public void displayItems(List<AttractionModel> data, ItemType itemType) {
        if (data != null) {
            if (this.data == null)
                this.data = data;
            else
                this.data.addAll(data);

            if (adapter == null) {
                adapter = new BrowseAttractionAdapter(this, this.data, itemType);
                binding.frBrowseRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void showDetail(AttractionModel model, View sharedView) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        // Pass data object in the bundle and populate details activity
        String transitionName = ViewCompat.getTransitionName(sharedView);
        intent.putExtra(getString(R.string.arg_model), model);
        intent.putExtra(getString(R.string.arg_trans), transitionName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), sharedView, ViewCompat.getTransitionName(sharedView));
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void showMenu(View anchor, final AttractionModel viewModel, final ItemType itemType) {

        final boolean isFav = viewModel.isFavourite();

        PopupMenu popupMenu = new PopupMenu(getActivity(), anchor);
        popupMenu.inflate(R.menu.menu_more);
        if (isFav) {
            MenuItem itemFav = popupMenu.getMenu().findItem(R.id.action_fav);
            if (itemFav != null) {
                itemFav.setTitle("Remove");
            }
        }
        if (itemType == ItemType.FAV) {
            MenuItem itemShare = popupMenu.getMenu().findItem(R.id.action_share);
            if (itemShare != null) {
                itemShare.setVisible(false);
            }
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_share:
                        break;
                    case R.id.action_fav:
                        if (isFav) {
                            App.getInstance().removeFromFav(viewModel);
                            viewModel.setFavourite(false);
                        } else {
                            App.getInstance().addToFav(viewModel);
                            viewModel.setFavourite(true);
                        }
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void showProgress() {
        if (!binding.swipeRefreshLayout.isRefreshing()) {
            binding.frBrowseRecyclerView.setVisibility(View.GONE);
            binding.frBrowseLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        binding.frBrowseLoadingProgress.setVisibility(View.GONE);
        binding.frBrowseRecyclerView.setVisibility(View.VISIBLE);
        if (binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(false);
    }

    public void showLoadMoreProgress() {
        if (adapter != null) {
            data.add(null);
            adapter.notifyItemInserted(data.size() - 1);
        }
    }

    public void hideLoadMoreProgress() {
        if (adapter != null) {
            data.remove(data.size() - 1);
            adapter.notifyItemRemoved(data.size());
        }
    }


    /**
     * Dont focus on this method
     * <p>
     * Just TEMP method to generate items to return like web service does
     */
    private List<AttractionModel> fetchItems(Context context, ItemType itemType, int pageCount) {

        int PAGE_SIZE = 20;
        List<AttractionModel> list = new ArrayList<>();

        if (pageCount < 3) {
            if (pageCount > 1)
                showLoadMoreProgress();
            else
                showProgress();

            if (itemType == ItemType.FAV) {
                list.addAll(App.getInstance().getFavItems());
            } else {
                int strTitleId, strDescId;
                int index;
                Random r = new Random();

                index = pageCount == 1 ? 1 : PAGE_SIZE + 1;
                strTitleId = R.string.str_title_travel;
                strDescId = R.string.str_description_travel;

                for (int i = index; i < index + PAGE_SIZE; i++) {
                    AttractionModel mode = new AttractionModel(i,
                            String.format(Locale.ENGLISH, context.getString(strTitleId), i),
                            String.format(Locale.ENGLISH, context.getString(strDescId), i),
                            imgTravel[r.nextInt(imgTravel.length)]);
                    list.add(mode);
                }
            }

            if (pageCount > 1)
                hideLoadMoreProgress();
            else
                hideProgress();
        }
        return list;
    }

    private int[] imgTravel = {
            R.drawable.img_travel1,
            R.drawable.img_travel2,
            R.drawable.img_travel3,
            R.drawable.img_travel4,
            R.drawable.img_travel5,
            R.drawable.img_travel6,
            R.drawable.img_travel7,
            R.drawable.img_travel8
    };
}
