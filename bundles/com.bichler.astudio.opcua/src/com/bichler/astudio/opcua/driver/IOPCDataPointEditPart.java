package com.bichler.astudio.opcua.driver;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Control;

import com.bichler.astudio.opcua.IOPCPerspectiveEditor;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.INamespaceTableChange;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public interface IOPCDataPointEditPart extends IOPCPerspectiveEditor, ISelectionProvider, INamespaceTableChange,
		IOPCEditorRefresh, IRemoteDriverModelChange {

	public void fillTriggerNodes(List<NodeToTrigger> nodesToTrigger);

	public void setDirty(boolean isDirty);

	/**
	 * Update graphical editor when changing an opc ua server
	 * 
	 * @param Element
	 */
	public void setDataPointSelection(Object element);

	public Control getDPControl();

	public ISelectionProvider getDPViewer();

	/**
	 * Context menu of all datapoint editors
	 */
//	public void hookDPContextMenu();

}
