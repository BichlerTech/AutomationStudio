package com.bichler.astudio.log.view.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class ASLogPreferenceManager {

	private IPreferenceStore preferenceStore;

	public ASLogPreferenceManager(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	public void addListener(IPropertyChangeListener preferenceListener) {
		this.preferenceStore.addPropertyChangeListener(preferenceListener);
	}

	public void removeListener(IPropertyChangeListener preferenceListener) {
		this.preferenceStore.removePropertyChangeListener(preferenceListener);
	}

	// public void storeColumnWidth(String columnName, int width) {
	//
	// }

	public int getLogBufferSize() {
		return this.preferenceStore
				.getInt(ASLogPreferenceConstants.PREF_BUFFER_SIZE);
	}

	public int getRefreshTime() {
		return this.preferenceStore
				.getInt(ASLogPreferenceConstants.PREF_SERVER_REFRESH_TIME);
	}

	public boolean isQuickSearchVisible() {
		return this.preferenceStore
				.getBoolean(ASLogPreferenceConstants.PREF_QUICKSEARCH_VISIBLE);
	}

	public int getLogServerPort() {
		return this.preferenceStore
				.getInt(ASLogPreferenceConstants.PREF_SERVER_PORT);
	}

	public void storeQuickSearchState(boolean visible) {
		this.preferenceStore
				.setValue(
						ASLogPreferenceConstants.PREF_QUICKSEARCH_VISIBLE,
						visible);
	}

	// public StructuralItem[] getStructuralItems() {
	// return null;
	// }

	// public boolean isAutoStart() {
	// return true;
	// }

	// public class StructuralItem {
	//
	// public String name = "";
	// public int width = 0;
	// public boolean display = true;
	//
	// public StructuralItem() {
	//
	// }
	//
	// @Override
	// public String toString() {
	// return "name=" + name + ", width=" + width + ", display=" + display;
	// }
	//
	// }
}
