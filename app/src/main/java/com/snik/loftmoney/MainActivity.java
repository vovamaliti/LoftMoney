package com.snik.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 100;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainPagesAdapter mainPagesAdapter;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private ActionMode actionMode = null;


    public Toolbar getToolbar() {
        return toolbar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new PageListener());
        tabLayout.setupWithViewPager(viewPager);
        floatingActionButton = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = viewPager.getCurrentItem();
                String type = null;
                switch (page) {
                    case MainPagesAdapter.PAGE_INCOMES:
                        type = Item.TYPE_INCOME;
                        break;
                    case MainPagesAdapter.PAGE_EXPENSES:
                        type = Item.TYPE_EXPENSE;
                        break;
                }
                if (type != null) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    intent.putExtra(AddActivity.KEY_TYPE, type);
                    startActivityForResult(intent, REQUEST_CODE);
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    class PageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
                case MainPagesAdapter.PAGE_INCOMES:
                case MainPagesAdapter.PAGE_EXPENSES:
                    floatingActionButton.show();
                    break;
                case MainPagesAdapter.PAGE_BALANCE:
                    floatingActionButton.hide();
                    break;

            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            switch (i) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                case ViewPager.SCROLL_STATE_SETTLING:
                    if (actionMode != null) {
                        actionMode.finish();
                        Log.i(TAG, "onPageScrollStateChanged: " + i);

                    }
                    break;

            }
        }
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        floatingActionButton.hide();
        tabLayout.setBackgroundColor(getResources().getColor(R.color.dark_grey_blue));
        actionMode = mode;

    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        floatingActionButton.show();
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        actionMode = null;

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");

        if (((App) getApplication()).isLoggedIn()) {
            initUi();
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }
    }

    private void initUi() {
            MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(adapter);

    }
}
