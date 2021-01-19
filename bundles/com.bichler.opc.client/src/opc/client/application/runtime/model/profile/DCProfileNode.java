package opc.client.application.runtime.model.profile;

import java.util.List;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.DataChangeRuntimeItem;
import opc.client.application.runtime.model.node.DataChangeRuntimeNode;

import org.jdom.Element;

public class DCProfileNode extends AbstractProfileNode {
	DCProfileNode() {
		super(ProjectsStoreConfiguration.DC);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		DataChangeRuntimeNode node = new DataChangeRuntimeNode();
		node.load(element);
		List<?> children = element.getChildren();
		for (Object child : children) {
			DataChangeRuntimeItem item = new DataChangeRuntimeItem();
			item.load((Element) child);
			node.addChild(item);
		}
		return node;
	}
}
