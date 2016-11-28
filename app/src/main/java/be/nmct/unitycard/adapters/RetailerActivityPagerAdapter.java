package be.nmct.unitycard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.fragments.customer.RetailerOffersFragment;

public class RetailerActivityPagerAdapter extends FragmentStatePagerAdapter {
    int mRetailerId;

    public RetailerActivityPagerAdapter(FragmentManager fm, int retailerId) {
        super(fm);
        this.mRetailerId = retailerId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RetailerInfoFragment.newInstance(mRetailerId);
            case 1:
                return RetailerOffersFragment.newInstance(mRetailerId);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Info"; // todo: naar string file (multi language)
            case 1:
                return "Aanbiedingen";
            default:
                return super.getPageTitle(position);
        }
    }
}
