package com.bichler.astudio.components.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "com.bichler.astudio.preferencepage.client";

	public static final String FIELD_SESSIONTIMEOUT = "SEPP_FIELD_SESSIONTIMEOUT";
	public static final String FIELD_OPERATIONTIMEOUT = "SEPP_FIELD_OPERATIONTIMEOUT";
	// public static final String FIELD_CONFIGFILE = "SEPP_FIELD_CONFIG";
	public static final String FIELD_KEEPALIVE = "SEPP_FIELD_KEEPALIVE";
	public static final String FIELD_RECONNECTINGPERIOD = "SEPP_FIELD_RECONNECT";
	public static final String FIELD_DISCOVERY = "SSEP_FIELD_VALIDDISCOVERY";

	/**
	 * Create the preference page.
	 */
	public GeneralPreferencePage() {
		super(GRID);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(FIELD_SESSIONTIMEOUT, CustomString
				.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.General.SessionTimeout"),
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(FIELD_SESSIONTIMEOUT, CustomString
				.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.General.SessionTimeout"),
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(FIELD_KEEPALIVE, CustomString
				.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.General.KeepAlive"),
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(FIELD_RECONNECTINGPERIOD,
				CustomString.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,
						"Preferences.General.ReconnectingInterval"),
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(FIELD_DISCOVERY, CustomString
				.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE, "Preferences.General.ValidDiscovery"),
				getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
		setPreferenceStore(ComponentsUIActivator.getDefault().getPreferenceStore());
	}
}
