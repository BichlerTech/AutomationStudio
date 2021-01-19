package com.bichler.astudio.opcua.opcmodeler.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.NodeIdMode;

public class ShowDialogPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PREFERENCESPACE = ", ";
	public static final String PREFERENCE_SHOWDIALOG_COPYPASTE = "showdialogcp";
	public static final String PREFERENCE_SHOWDIALOG_DELETE = "showdialogdelete";
	public static final String PREFERENCE_NODEID_FILL_GAPES = "nodeidfillgapes";
	public static final String PREFERENCE_OPCUA_EDIT_INTERNAL = "opcuaeditinternal";
	// public static final String PREFERENCE_OPCUA_DO_COMPILEL = "modeldocompile";

	public static String getLine(String[] namespaceEntry) {
		if (namespaceEntry != null) {
			int index = Integer.parseInt(namespaceEntry[0]);
			String ns = namespaceEntry[1];
			return index + PREFERENCESPACE + ns;
		}
		return null;
	}

	public static String[] splitLine(String line) {
		return line.split(PREFERENCESPACE);
	}

	public ShowDialogPreferencePage() {
		super(GRID);
		setTitle("Dialogs");
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("HB Studio Designer");
	}

	@Override
	protected void createFieldEditors() {
		String[][] entries = new String[NodeIdMode.values().length][2];
		for (int i = 0; i < NodeIdMode.values().length; i++) {
			entries[i][0] = NodeIdMode.values()[i].name();
			entries[i][1] = "" + i;
		}
		addField(new BooleanFieldEditor(PREFERENCE_SHOWDIALOG_COPYPASTE, "Show Copy/Paste Dialog",
				BooleanFieldEditor.DEFAULT, getFieldEditorParent()));
		addField(new BooleanFieldEditor(PREFERENCE_SHOWDIALOG_DELETE, "Show Delete Dialog", BooleanFieldEditor.DEFAULT,
				getFieldEditorParent()));
		addField(new ComboFieldEditor(PREFERENCE_NODEID_FILL_GAPES, "Automatische NodeIdvergabe", entries,
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(PREFERENCE_OPCUA_EDIT_INTERNAL, "OPC UA interne Elemente editieren",
				BooleanFieldEditor.DEFAULT, getFieldEditorParent()));
	}
}
