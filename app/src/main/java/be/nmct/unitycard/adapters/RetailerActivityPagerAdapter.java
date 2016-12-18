package be.nmct.unitycard.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.nmct.unitycard.R;
import be.nmct.unitycard.fragments.customer.RetailerInfoFragment;
import be.nmct.unitycard.fragments.customer.RetailerOffersFragment;

public class RetailerActivityPagerAdapter extends FragmentStatePagerAdapter {
    int mRetailerId;
    Context mContext;

    public RetailerActivityPagerAdapter(FragmentManager fm, int retailerId, Context context) {
        super(fm);
        this.mRetailerId = retailerId;
        this.mContext = context;
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
                return mContext.getString(R.string.info);
            case 1:
                return mContext.getString(R.string.aanbiedingen);
            default:
                return super.getPageTitle(position);
        }
    }
}
