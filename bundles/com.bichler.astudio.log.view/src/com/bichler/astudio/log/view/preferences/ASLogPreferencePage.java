package com.bichler.astudio.log.view.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.log.view.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class ASLogPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "com.hbsoft.comet.log4j.view.preferences.CometLogPreferencePage";

	public ASLogPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "preference.page.log"));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new IntegerFieldEditor(ASLogPreferenceConstants.PREF_SERVER_PORT,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "preference.page.log.port"),
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(ASLogPreferenceConstants.PREF_BUFFER_SIZE,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "preference.page.log.message"),
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(ASLogPreferenceConstants.PREF_SERVER_REFRESH_TIME,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "preference.page.log.update"),
				getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}