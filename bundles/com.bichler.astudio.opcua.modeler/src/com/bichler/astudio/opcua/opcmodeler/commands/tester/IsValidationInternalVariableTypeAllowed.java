package com.bichler.astudio.opcua.opcmodeler.commands.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableTypeInternalModelNode;

public class IsValidationInternalVariableTypeAllowed extends PropertyTester {
	public static final String PROPERTY_NAMESPACE = "com.xcontrol.modeler.opc.tester";
	public static final String PROPERTY_CAN_SHOWVARIABLETYPE = "canShowVariableType";

	public IsValidationInternalVariableTypeAllowed() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver != null && receiver instanceof TreeSelection) {
			Object obj = ((TreeSelection) receiver).getFirstElement();
			if (obj != null && obj instanceof BrowserVariableTypeInternalModelNode) {
				IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
				if (preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
					return true;
				}
			}
		}
		return false;
	}
}
