package com.snik.loftmoney.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.snik.loftmoney.R;
import com.snik.loftmoney.fragment.BalanceFragment;
import com.snik.loftmoney.fragment.ItemsFragment;
import com.snik.loftmoney.model.Item;

public class MainPagesAdapter extends FragmentPagerAdapter {

    public static final int PAGE_INCOMES = 0;
    public static final int PAGE_EXPENSES = 1;
    public static final int PAGE_BALANCE = 2;
    private static final int PAGES_COUNT = 3;
    private String[] pagesTitles;

    public MainPagesAdapter(FragmentManager fm, Context context) {
        super(fm);
        pagesTitles = context.getResources().getStringArray(R.array.main_tabs);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case PAGE_INCOMES:
                return ItemsFragment.newInstance(Item.TYPE_INCOME);
            case PAGE_EXPENSES:
                return ItemsFragment.newInstance(Item.TYPE_EXPENSE);
            case PAGE_BALANCE:
                return BalanceFragment.newInstance();
            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return pagesTitles[position];
    }
}