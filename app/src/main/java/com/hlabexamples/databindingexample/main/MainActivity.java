package com.hlabexamples.databindingexample.main;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hlabexamples.databindingexample.R;
import com.hlabexamples.databindingexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (getIntent() != null) {
            binding.setName(getIntent().getStringExtra(getString(R.string.arg_name)));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragmentWithType(ItemType.HOME);
                    return true;
                case R.id.navigation_fav:
                    showFragmentWithType(ItemType.FAV);
                    return true;
            }
            return false;
        }

    };

    private void showFragmentWithType(ItemType type) {
        Fragment fragment = new BrowseAttractionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.arg_type), type);
        fragment.setArguments(bundle);
        replaceFragment(this, R.id.container, fragment);
    }


    private void replaceFragment(Activity context, int container, Fragment targetFragment) {
        context.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.enter,
                        R.animator.exit,
                        0, 0)
                .replace(container, targetFragment, targetFragment.getClass().getSimpleName())
                .commit();
    }
}
