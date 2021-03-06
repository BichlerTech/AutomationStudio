package opc.client.application.runtime.model.node;

import org.jdom.Element;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;

public class AbstractRuntimeItem extends AbstractIdentifiedNode {
	private String displayname = "";
	private String nodeId = "";

	public AbstractRuntimeItem() {
		super();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrDisplayname = element.getAttributeValue(ProjectsStoreConfiguration.DISPLAYNAME.name());
		setDisplayname(attrDisplayname);
		String attrNodeId = element.getAttributeValue(ProjectsStoreConfiguration.NODEID.name());
		setNodeId(attrNodeId);
	}

	public String getDisplayname() {
		return displayname;
	}

	public String getNodeId() {
		return nodeId;
	}

	private void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	private void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
}
