package com.bichler.astudio.opcua.opcmodeler.commands.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeSelection;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.preferences.ShowDialogPreferencePage;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserEventTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserFolderInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectTypeInternalModelNode;

public class IsValidationInternalObjectTypeAllowed extends PropertyTester {
	public static final String PROPERTY_NAMESPACE = "com.xcontrol.modeler.opc.tester";
	public static final String PROPERTY_CAN_SHOWOBJECTTYPE = "canShowObjectType";

	public IsValidationInternalObjectTypeAllowed() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver != null && receiver instanceof TreeSelection) {
			Object obj = ((TreeSelection) receiver).getFirstElement();
			if (obj != null && obj instanceof BrowserObjectTypeInternalModelNode
					|| obj instanceof BrowserFolderInternalModelNode
					|| obj instanceof BrowserEventTypeInternalModelNode) {
				IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
				if (preferenceStore.getBoolean(ShowDialogPreferencePage.PREFERENCE_OPCUA_EDIT_INTERNAL)) {
					return true;
				}
			}
		}
		return false;
	}
}
