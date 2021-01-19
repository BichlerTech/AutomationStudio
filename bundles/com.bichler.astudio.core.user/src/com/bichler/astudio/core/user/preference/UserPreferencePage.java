package com.bichler.astudio.core.user.preference;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.core.user.UserActivator;
import com.bichler.astudio.core.user.type.AbstractStudioUser;
import com.bichler.astudio.core.user.widget.UserComboFieldEditor;
import com.bichler.astudio.utils.constants.StudioConstants;
import com.bichler.astudio.utils.internationalization.CustomString;

import org.eclipse.jface.preference.ComboFieldEditor;

public class UserPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "com.bichler.astudio.user";
	private UserComboFieldEditor fieldEditor;
	private String oldUser;
	private String newUser;

	/**
	 * Create the preference page.
	 */
	public UserPreferencePage() {
		super(GRID);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		/*this.fieldEditor = new IntegerFieldEditor(StudioConstants.StudioUser,
				CustomString.getString(UserActivator.getDefault().RESOURCE_BUNDLE, "preferences.user.name"),
				getFieldEditorParent());

		addField(fieldEditor);*/
		this.fieldEditor = new UserComboFieldEditor(StudioConstants.StudioUser,
				CustomString.getString(UserActivator.getDefault().RESOURCE_BUNDLE, "preferences.user.name"),
				new String[][] { { "User", "" + AbstractStudioUser.TYPE_USER_DEFAULT },
						{ "Admin", "" + AbstractStudioUser.TYPE_USER_SUPER } },
				getFieldEditorParent());
		
		addField(fieldEditor);		
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
		setPreferenceStore(UserActivator.getDefault().getPreferenceStore());
	}

	@Override
	public boolean performOk() {
		this.oldUser = getPreferenceStore().getString(StudioConstants.StudioUser);
		this.newUser = this.fieldEditor.getSelectedValue();

		if (!oldUser.trim().equals(newUser.trim())) {
			boolean confirmed = MessageDialog.openConfirm(getShell(), "Confirm restart",
					"User has changed. Automation Studio needs to restart to continue!");
			if (!confirmed) {
				return false;
			}

			boolean result = super.performOk();
			PlatformUI.getWorkbench().restart();
			return result;
		}

		return super.performOk();
	}
}
