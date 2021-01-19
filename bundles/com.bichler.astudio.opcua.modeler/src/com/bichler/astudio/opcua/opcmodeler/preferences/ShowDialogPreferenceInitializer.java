package com.bichler.astudio.opcua.opcmodeler.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.bichler.astudio.opcua.opcmodeler.Activator;

public class ShowDialogPreferenceInitializer extends AbstractPreferenceInitializer {
	public ShowDialogPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(ShowDialogPreferencePage.PREFERENCE_SHOWDIALOG_COPYPASTE, true);
		store.setDefault(ShowDialogPreferencePage.PREFERENCE_SHOWDIALOG_DELETE, true);
		store.setDefault(ShowDialogPreferencePage.PREFERENCE_NODEID_FILL_GAPES, 0);
		store.setDefault(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL, false);
	}
}
