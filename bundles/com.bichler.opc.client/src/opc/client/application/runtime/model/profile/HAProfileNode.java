package opc.client.application.runtime.model.profile;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.HistoricalRuntimeItem;

import org.jdom.Element;

public class HAProfileNode extends AbstractProfileNode {
	HAProfileNode() {
		super(ProjectsStoreConfiguration.HA);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		HistoricalRuntimeItem node = new HistoricalRuntimeItem();
		node.load(element);
		addChild(node);
		return node;
	}
}
