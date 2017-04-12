package com.hlabexamples.databindingexample.detail;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlabexamples.databindingexample.R;
import com.hlabexamples.databindingexample.main.AttractionModel;


public class DetailActivity extends AppCompatActivity {

    ImageView imgMain;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initToolBar();
        initializeComponent();
    }

    private void initializeComponent() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Match the detail image view transition name with the original
        imgMain = (ImageView) findViewById(R.id.img_detail);
        tv = (TextView) findViewById(R.id.tv);
        ViewCompat.setTransitionName(imgMain, getIntent().getStringExtra(getString(R.string.arg_trans)));

        // Postpone the transition until the detail image thumbnail is loaded
        ActivityCompat.postponeEnterTransition(this);

        if (getIntent() != null && getIntent().hasExtra(getString(R.string.arg_model))) {
            AttractionModel model = getIntent().getParcelableExtra(getString(R.string.arg_model));
//            imgMain.setImageResource(model.getImgDrawableId());
            tv.setText(model.getItemDescription());
            getSupportActionBar().setTitle(model.getItemTitle());
        }

        scheduleStartPostponedTransition(imgMain);

    }

    protected void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void scheduleStartPostponedTransition(final View sharedElement) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
