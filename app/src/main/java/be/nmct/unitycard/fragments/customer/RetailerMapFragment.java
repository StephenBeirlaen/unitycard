package be.nmct.unitycard.fragments.customer;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import be.nmct.unitycard.models.RetailerLocation;
import be.nmct.unitycard.repositories.GeoCodeRepository;

public class RetailerMapFragment extends SupportMapFragment
        implements OnMapReadyCallback {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private RetailerMapFragmentListener mListener;
    private GoogleMap mGoogleMap;
    private ArrayList<RetailerLocation> mRetailerLocations;
    private RetailerLocation mClosestRetailerLocation;

    public RetailerMapFragment() {
        // Required empty public constructor
    }

    public static final String ARG_RETAILER_LOCATION = "be.nmct.unitycard.ARG_RETAILER_LOCATION";
    public static final String ARG_RETAILER_LOCATION_LIST = "be.nmct.unitycard.ARG_RETAILER_LOCATION_LIST";

    public static RetailerMapFragment newInstance(Bundle args) {
        RetailerMapFragment fragment = new RetailerMapFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();

        mClosestRetailerLocation = args.getParcelable(ARG_RETAILER_LOCATION);
        mRetailerLocations = args.getParcelableArrayList(ARG_RETAILER_LOCATION_LIST);

        getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        GeoCodeRepository geoCodeRepository = new GeoCodeRepository(getContext());
        try {
            if (mClosestRetailerLocation != null) {
                final String address = mClosestRetailerLocation.getStreet() + " "
                        + mClosestRetailerLocation.getNumber() + " "
                        + mClosestRetailerLocation.getZipcode() + " "
                        + mClosestRetailerLocation.getCity() + " "
                        + mClosestRetailerLocation.getCountry();

                geoCodeRepository.requestLatLong(URLEncoder.encode(address, "utf-8"), new GeoCodeRepository.GeoCodeResponseListener() {
                    @Override
                    public void latLongReceived(double lat, double lng) {
                        toonLocatie(lat, lng, mClosestRetailerLocation.getName(), address, true);
                        moveCamera(lat, lng);
                    }

                    @Override
                    public void geoCodeRequestError(String error) {
                        mListener.handleError(error);
                    }
                });
            }
            else if (mRetailerLocations != null && mRetailerLocations.size() > 0) {
                final int locationCount = mRetailerLocations.size();

                final LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

                for (final RetailerLocation loc: mRetailerLocations) {
                    final String address = loc.getStreet() + " "
                            + loc.getNumber() + " "
                            + loc.getZipcode() + " "
                            + loc.getCity() + " "
                            + loc.getCountry();

                    geoCodeRepository.requestLatLong(URLEncoder.encode(address, "utf-8"), new GeoCodeRepository.GeoCodeResponseListener() {
                        @Override
                        public void latLongReceived(double lat, double lng) {
                            toonLocatie(lat, lng, loc.getName(), address, false);
                            boundsBuilder.include(new LatLng(lat, lng));

                            // Is dit het laatste element in de lijst?
                            if (mRetailerLocations.indexOf(loc) == locationCount - 1) {
                                LatLngBounds bounds = boundsBuilder.build();
                                moveCameraCenteredAroundBounds(bounds);
                            }
                        }

                        @Override
                        public void geoCodeRequestError(String error) {
                            mListener.handleError(error);
                        }
                    });
                }
            }
            else {
                mListener.handleError("Error opening map!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void toonLocatie(double latitude, double longitude, String retailerName, String address, Boolean showInfoWindow) {
        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(retailerName)
                .snippet(address)
        );

        if (showInfoWindow) {
            marker.showInfoWindow();
        }
    }

    private void moveCamera(double latitude, double longitude) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
    }

    private void moveCameraCenteredAroundBounds(LatLngBounds latLngBounds) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30));
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
