package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import be.nmct.unitycard.R;
import be.nmct.unitycard.repositories.GeoCodeRepository;

public class RetailerMapFragment extends SupportMapFragment
        implements OnMapReadyCallback {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerMapFragmentListener mListener;
    private GoogleMap mGoogleMap;
    private String mRetailerName;
    private String mAdres;

    public RetailerMapFragment() {
        // Required empty public constructor
    }

    public static final String ARG_RETAILER_NAME = "be.howest.nmct.kotwest.ARG_RETAILER_NAME";
    public static final String ARG_ADDRESS = "be.howest.nmct.kotwest.ARG_ADDRESS";

    public static RetailerMapFragment newInstance(String retailerName, String address) {
        RetailerMapFragment fragment = new RetailerMapFragment();

        Bundle args = new Bundle();
        args.putString(ARG_RETAILER_NAME, retailerName);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        mRetailerName = args.getString(ARG_RETAILER_NAME);
        mAdres = args.getString(ARG_ADDRESS);

        getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // todo: perform load actions here
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        GeoCodeRepository geoCodeRepository = new GeoCodeRepository(getContext());
        try {
            geoCodeRepository.requestLatLong(URLEncoder.encode(mAdres, "utf-8"), new GeoCodeRepository.GeoCodeResponseListener() {
                @Override
                public void latLongReceived(double lat, double lng) {
                    toonLocatie(lat, lng);
                }

                @Override
                public void geoCodeRequestError(String error) {
                    mListener.handleError(error);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void toonLocatie(double latitude, double longitude) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(mRetailerName)
                .snippet(mAdres)
        ).showInfoWindow();
    }

    public interface RetailerMapFragmentListener {
        void handleError(String error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // zeker zijn dat de container activity de callback geimplementeerd heeft
        if (context instanceof RetailerMapFragmentListener) {
            mListener = (RetailerMapFragmentListener)context;
        } else {
            throw new ClassCastException(context.toString() + "must implement " + RetailerMapFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
