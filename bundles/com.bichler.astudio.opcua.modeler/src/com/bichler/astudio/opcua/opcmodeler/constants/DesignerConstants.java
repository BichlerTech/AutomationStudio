package com.bichler.astudio.opcua.opcmodeler.constants;

import java.util.HashSet;
import java.util.Set;

import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorDataTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorMethodPart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorObjectPart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorObjectTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorReferenceTypePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorVariablePart;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorVariableTypePart;

public abstract class DesignerConstants {
	public static final String COMMAND_UPDATE_NAMESPACETABLE = "command.update.opcua.namespacetable";
	public static final String[] EXTENSION_INFORMATIONMODEL = { "*.xml", "*.jar" };
	public static final String[] EXTENSION_NAME_INFORMATIONMODEL = { "XML", "JAR" };
	public static final String SERVERCONFIG_PATH = "configuration/serverconfig.xml";
	public static final String PREDEFINED_OPC_NODES = "configuration/nodeset.xml";
	// auto load at startup
	public static final String PREDEFINED_DI_PLC_NODES = "configurations/DI-PlcOpen.xml";
	public static final String MODELEDITVIEW_ID = "modeledit";
	public static final String NODEEDITOR_ID = "modelEditor";
	public static final String COMMAND_REFRESHMODELVIEWTREE = "commands.designer.modelview.refresh";
	public static final Set<String> NODEEDITORIDS = new HashSet<>();
	static {
		NODEEDITORIDS.add(NodeEditorObjectPart.ID);
		NODEEDITORIDS.add(NodeEditorVariablePart.ID);
		NODEEDITORIDS.add(NodeEditorDataTypePart.ID);
		NODEEDITORIDS.add(NodeEditorReferenceTypePart.ID);
		NODEEDITORIDS.add(NodeEditorObjectTypePart.ID);
		NODEEDITORIDS.add(NodeEditorMethodPart.ID);
		NODEEDITORIDS.add(NodeEditorVariableTypePart.ID);
	}
}
