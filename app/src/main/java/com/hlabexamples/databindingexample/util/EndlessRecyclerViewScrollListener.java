package com.hlabexamples.databindingexample.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created in BindingConstraintMVP-Demo on 13/01/17.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;

    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    // Sets the  footerViewType
    private int defaultNoFooterViewType = -1;
    private int footerViewType = -1;


    private String mTag = EndlessRecyclerViewScrollListener.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;

    protected EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        init();
        this.mLayoutManager = layoutManager;
    }

    protected EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        init();
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    protected EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        init();
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    //init from  self-define
    private void init() {
        footerViewType = getFooterViewType(defaultNoFooterViewType);
        startingPageIndex = getStartingPageIndex();

        int threshold = getVisibleThreshold();
        if (threshold > visibleThreshold) {
            visibleThreshold = threshold;
        }
    }


    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(final RecyclerView view, int dx, int dy) {
        if (dy <= 0) return;

        RecyclerView.Adapter adapter = view.getAdapter();
        int totalItemCount = adapter.getItemCount();

        int lastVisibleItemPosition = getLastVisibleItemPosition();

        boolean isAllowLoadMore = (lastVisibleItemPosition + visibleThreshold) > totalItemCount;

        if (isAllowLoadMore) {

            if (isUseFooterView()) {
                if (!isFooterView(adapter)) {

                    if (totalItemCount < previousTotalItemCount) {//swiprefresh reload result to change listsize ,reset pageindex
                        this.currentPage = this.startingPageIndex;
                    } else if (totalItemCount == previousTotalItemCount) {//if load failure or load empty data , we rollback  pageindex
                        currentPage = currentPage == startingPageIndex ? startingPageIndex : --currentPage;
                    }

                    loading = false;
                }
            } else {
                if (totalItemCount > previousTotalItemCount) loading = false;
            }

            if (!loading) {

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too

                previousTotalItemCount = totalItemCount;
                currentPage++;
                onLoadMore(currentPage, totalItemCount);
                loading = true;
                Log.i(mTag, "request pageindex:" + currentPage + ",totalItemsCount:" + totalItemCount);

            }
        }
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    private boolean isUseFooterView() {
        return footerViewType != defaultNoFooterViewType;
    }

    private boolean isFooterView(RecyclerView.Adapter padapter) {
        boolean isFooterView = false;
        int ptotalItemCount = padapter.getItemCount();
        if (ptotalItemCount > 0) {
            int lastPosition = ptotalItemCount - 1;
            int lastViewType = padapter.getItemViewType(lastPosition);
            isFooterView = lastViewType == footerViewType;
        }
        return isFooterView;
    }

    private int getLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;

        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // set FooterView type
    // if don't use footview loadmore  default: -1
    public abstract int getFooterViewType(int defaultNoFooterViewType);

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);

    //set visibleThreshold   default: 5
    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    //set startingPageIndex   default: 0
    public int getStartingPageIndex() {
        return startingPageIndex;
    }

}