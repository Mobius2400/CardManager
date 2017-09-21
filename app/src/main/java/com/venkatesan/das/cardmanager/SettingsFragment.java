package com.venkatesan.das.cardmanager;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import com.venkatesan.das.cardmanager.ZipCodeAPI;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment {
    private String results = "Try again. Something went wrong.";
    private String city_state, zipCode;
    private SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        pref = getActivity().getSharedPreferences(Contract.sharedPreferences, MODE_PRIVATE);

        //Set Name
        EditTextPreference name = (EditTextPreference) findPreference("user_name");
        name.setSummary(pref.getString(Contract.userName, ""));
        name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });

        //Set Email Address
        EditTextPreference email = (EditTextPreference) findPreference("email_address");
        email.setSummary(pref.getString(Contract.email, ""));
        email.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });

        //Set Location
        EditTextPreference location = (EditTextPreference) findPreference("location");
        location.setSummary(pref.getString(Contract.location, ""));
        location.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                zipCode = (String) newValue;
                try {
                    new asyncZipToLocation(preference).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return view;
    }

    private class asyncZipToLocation extends AsyncTask<Void, Void, Void> {
        Preference myPref;

        public asyncZipToLocation(Preference preference) {
            myPref = preference;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                results = ZipCodeAPI.locationByZip(zipCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsnObj = new JSONObject(results);
                String city = jsnObj.getString(Contract.city);
                String state = jsnObj.getString(Contract.state);
                city_state = city + ", " + state;
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Contract.location, city_state);
                editor.commit();
                myPref.setSummary(city_state);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
