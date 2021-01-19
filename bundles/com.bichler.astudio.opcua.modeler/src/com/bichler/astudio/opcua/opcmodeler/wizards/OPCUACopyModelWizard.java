package com.bichler.astudio.opcua.opcmodeler.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.NodeClass;

import opc.sdk.core.node.Node;

public class OPCUACopyModelWizard extends Wizard {
	// wizard pages
	private CopyWizardNamespacePage namespacePage;
	private CopyWizardDetailPage detailPage;
	// parent node
	private Node parent;
	// node attributes
	private PasteStartNode startNode;
	private String content = null;

	// private NodeId nodeId;
	// private QualifiedName browsename;
	// private LocalizedText description;
	// private LocalizedText displayname;
	// private PasteStartNode startNode;
	public OPCUACopyModelWizard(Node parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void addPages() {
		NodeId node2copy = parseClipboardInformation(this.content);
		this.namespacePage = new CopyWizardNamespacePage(this.parent);
		this.namespacePage.setTitle("title");
		this.namespacePage.setDescription("description");
		addPage(this.namespacePage);
		this.detailPage = new CopyWizardDetailPage(node2copy, this.namespacePage);
		this.detailPage.setTitle("details");
		this.detailPage.setDescription("detail description");
		this.namespacePage.setDetailPage(this.detailPage);
		addPage(this.detailPage);
		// TODO: cannot copy inside same namespace if flag is copyIds = true;
	}

	@Override
	public boolean performFinish() {
		QualifiedName browsename = this.detailPage.getBrowseName();
		LocalizedText description = this.detailPage.getDescriptionText();
		LocalizedText displayname = this.detailPage.getDisplayname();
		NodeId nodeId = this.detailPage.getNodeId();
		this.startNode = new PasteStartNode(browsename, description, displayname, nodeId);
		this.startNode.setCopyIds(this.detailPage.isUseSameIds());
		return true;
	}

	public PasteStartNode getStartNode() {
		return this.startNode;
	}

	public void setContents(String content) {
		this.content = content;
	}

	/**
	 * Parses the text clipboard information
	 * 
	 * @param content
	 * @return
	 */
	private NodeId parseClipboardInformation(String content) {
		NodeId nodeId = NodeId.NULL;
		String[] nodeTextInformation = ((String) content).split("\n");
		// TODO: BUT IT IS ONLY SINGLE SELECTION
		/** fetch copy info from clipboard */
		for (int i = 0; i < nodeTextInformation.length; i++) {
			String info = nodeTextInformation[i];
			String[] splitted = info.split("\t");
			/** does not match [nodeid \t nodeclass] \t iscopy \n */
			if (splitted.length < 3) {
				continue;
			}
			nodeId = NodeId.parseNodeId(splitted[0]);
			NodeClass nodeClass = NodeClass.valueOf(new Integer(splitted[1]));
			// map.put(nodeId, nodeClass);
			// depricated is copy
			Boolean.parseBoolean(splitted[2]);
		}
		return nodeId;
	}
}
