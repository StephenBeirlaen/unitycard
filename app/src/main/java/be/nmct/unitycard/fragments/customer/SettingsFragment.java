package be.nmct.unitycard.fragments.customer;


import android.accounts.Account;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.Map;

import be.nmct.unitycard.R;
import be.nmct.unitycard.auth.AuthHelper;
import be.nmct.unitycard.helpers.FcmTokenHelper;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String KEY_PREF_NOTIFICATIONS = "pref_filter_notifications";

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        onSharedPreferenceChanged(sharedPref, KEY_PREF_NOTIFICATIONS);

        setSummaries(sharedPref);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummary(sharedPreferences, key);

        if (key.equals(KEY_PREF_NOTIFICATIONS)) {
            CheckBoxPreference notificationsEnabled = (CheckBoxPreference) findPreference(key);
            if (notificationsEnabled.isChecked()) {
                // Set Firebase Cloud Messaging Token
                String fcmToken = FirebaseInstanceId.getInstance().getToken();
                FcmTokenHelper.sendRegistrationToServer(fcmToken, getActivity());
            }
            else {
                Account user = AuthHelper.getUser(getActivity());
                if (user != null) {
                    AuthHelper.getAccessToken(user, getActivity(), new AuthHelper.GetAccessTokenListener() {
                        @Override
                        public void tokenReceived(String accessToken) {
                            FcmTokenHelper.removeRegistrationToken(getActivity(), accessToken);
                        }

                        @Override
                        public void requestNewLogin() {

                        }
                    });
                }
            }
        }
    }

    private void setSummaries(SharedPreferences sharedPreferences) {
        Map<String, ?> prefs = sharedPreferences.getAll();
        for (Map.Entry<String, ?> pref: prefs.entrySet()) {
            setSummary(sharedPreferences, pref.getKey());
        }
    }

    private void setSummary(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_NOTIFICATIONS)) {
            Preference preference = findPreference(key);
            // Set summary to be the user-description for the selected value
            Boolean result = sharedPreferences.getBoolean(key, true);
            if (result) {
                preference.setSummary(getResources().getString(R.string.notificationsEnabled));
            } else
                preference.setSummary(getResources().getString(R.string.notificationsDisabled));
        }
    }
}
