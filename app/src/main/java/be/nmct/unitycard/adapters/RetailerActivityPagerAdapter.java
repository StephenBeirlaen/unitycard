package be.nmct.unitycard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.nmct.unitycard.fragments.RetailerInfoFragment;
import be.nmct.unitycard.fragments.RetailerOffersFragment;

public class RetailerActivityPagerAdapter extends FragmentStatePagerAdapter {
    public RetailerActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RetailerInfoFragment.newInstance();
            case 1:
                return RetailerOffersFragment.newInstance();
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
