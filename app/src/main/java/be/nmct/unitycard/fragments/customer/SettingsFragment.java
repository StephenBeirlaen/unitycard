package be.nmct.unitycard.fragments.customer;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import be.nmct.unitycard.R;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    //private static final String KEY_PREF_LIST = "pref_filterType";
    // todo: update this whole class

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        /*SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
        setSummaries(sharedPref);*/

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //onSharedPreferenceChanged(sharedPref, KEY_PREF_LIST);

        //setSummaries(sharedPref);
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
        //setSummary(sharedPreferences, key);
    }

    /*private void setSummaries(SharedPreferences sharedPreferences) {
        Map<String, ?> prefs = sharedPreferences.getAll();
        for (Map.Entry<String, ?> pref: prefs.entrySet()) {
            setSummary(sharedPreferences, pref.getKey());
        }
    }

    private void setSummary(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_LIST)) {
            Preference preference = findPreference(key);
            // Set summary to be the user-description for the selected value
            String[] values = getResources().getStringArray(R.array.listvalues);
            int pos = Arrays.asList(values).indexOf(sharedPreferences.getString(key, ""));
            if (pos >=0) {
                preference.setSummary(getResources().getStringArray(R.array.filterentries)[pos]);
            } else
                preference.setSummary("");
        }
    }*/
}
