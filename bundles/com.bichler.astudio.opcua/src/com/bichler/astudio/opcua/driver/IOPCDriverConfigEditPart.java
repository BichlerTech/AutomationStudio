package com.bichler.astudio.opcua.driver;

import java.util.List;

import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public interface IOPCDriverConfigEditPart extends IOPCPerspectiveEditor, IOPCEditorRefresh, IRemoteDriverModelChange {

	public void setDirty(boolean isDirty);

	public void computeSection();

	public boolean isTriggerNodeValid(NodeToTrigger obj);

	public List<NodeToTrigger> getPossibleTriggerNodes();

	public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes);
}
