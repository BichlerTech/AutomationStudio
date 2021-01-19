package com.bichler.astudio.opcua.opcmodeler.commands.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.preference.IPreferenceStore;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;

public class IsInternalAllowed extends PropertyTester {
	public static final String PROPERTY_NAMESPACE = "com.xcontrol.modeler.opc.tester";
	public static final String PROPERTY_CAN_SHOWINTERNAL = "canShowInternal";

	public IsInternalAllowed() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		if (preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
			return true;
		}
		return false;
	}
}
