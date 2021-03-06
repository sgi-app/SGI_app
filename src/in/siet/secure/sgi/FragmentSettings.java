package in.siet.secure.sgi;

import in.siet.secure.Util.Utility;
import in.siet.secure.contants.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class FragmentSettings extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	public static final String TAG = "in.siet.secure.sgi.FragmentSettings";
	private SharedPreferences spf;

	public FragmentSettings() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.fragment_settings);
		// setRetainInstance(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		ActionBar toolbar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		toolbar.setTitle(R.string.action_settings);
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref,
			String key) {
		if (key.equals(getString(R.string.pref_key_server_ip))) {
			// Set summary to be the user-description for the selected value
			String ip = sharedPref.getString(key, "");
			// Utility.SERVER = ip;
			getSPreferences()
					.edit()
					.putString(Constants.PREF_KEYS.SERVER_IP,
							sharedPref.getString(key, "")).commit();

			(findPreference(getString(R.string.pref_key_server_ip)))
					.setSummary(ip);

			Utility.RaiseToast(getActivity(), "Server's IP Address updated",
					false);

		} else if (key.equals(getString(R.string.pref_key_update_interval))) {
			// useless preference now as we implemented GCM
			getSPreferences()
					.edit()
					.putString(Constants.PREF_KEYS.UPDATE_INTERVAL,
							sharedPref.getString(key, String.valueOf(1)))
					.commit();

		} else if (key.equals(getString(R.string.pref_key_local_server))) {
			getSPreferences()
					.edit()
					.putBoolean(Constants.PREF_KEYS.LOCAL_SERVER,
							sharedPref.getBoolean(key, false)).commit();
		}
	}

	private SharedPreferences getSPreferences() {
		if (spf == null)
			spf = getActivity().getSharedPreferences(Constants.PREF_FILE_NAME,
					Context.MODE_PRIVATE);
		return spf;
	}
}
