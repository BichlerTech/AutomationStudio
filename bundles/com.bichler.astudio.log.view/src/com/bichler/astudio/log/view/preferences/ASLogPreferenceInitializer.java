package com.bichler.astudio.log.view.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.bichler.astudio.log.view.Activator;

public class ASLogPreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(ASLogPreferenceConstants.PREF_BUFFER_SIZE, 100);
		store.setDefault(ASLogPreferenceConstants.PREF_SERVER_PORT, 12345);
		store.setDefault(
				ASLogPreferenceConstants.PREF_QUICKSEARCH_VISIBLE, false);
		store.setDefault(
				ASLogPreferenceConstants.PREF_SERVER_REFRESH_TIME, 1000);
	}

}
