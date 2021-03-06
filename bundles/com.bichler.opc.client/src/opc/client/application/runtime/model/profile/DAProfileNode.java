package opc.client.application.runtime.model.profile;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.DataAccessRuntimeItem;

import org.jdom.Element;

public class DAProfileNode extends AbstractProfileNode {
	DAProfileNode() {
		super(ProjectsStoreConfiguration.DA);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		DataAccessRuntimeItem node = new DataAccessRuntimeItem();
		node.load(element);
		addChild(node);
		return node;
	}
}
