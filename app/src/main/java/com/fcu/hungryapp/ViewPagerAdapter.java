package com.fcu.hungryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final String shopId;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String shopId) {
        super(fragmentActivity);
        this.shopId = shopId;
    }

    public ViewPagerAdapter(@NonNull Fragment fragment, String shopId) {
        super(fragment);
        this.shopId = shopId;
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String shopId) {
        super(fragmentManager, lifecycle);
        this.shopId = shopId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment fragment = new ReserveFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SearchShop.SHOP_ID_VALUE, shopId);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                Fragment fragment2 = new OrderFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(SearchShop.SHOP_ID_VALUE, shopId);
                fragment2.setArguments(bundle2);
                return fragment2;
            default:
                return new ReserveFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
