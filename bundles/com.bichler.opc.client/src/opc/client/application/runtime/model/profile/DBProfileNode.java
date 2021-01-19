package opc.client.application.runtime.model.profile;

import java.util.List;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.DatabaseRuntimeItem;
import opc.client.application.runtime.model.node.DatabaseRuntimeNode;

import org.jdom.Element;

public class DBProfileNode extends AbstractProfileNode {
	DBProfileNode() {
		super(ProjectsStoreConfiguration.DB);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		DatabaseRuntimeNode node = new DatabaseRuntimeNode();
		node.load(element);
		List<?> children = element.getChildren();
		for (Object child : children) {
			DatabaseRuntimeItem item = new DatabaseRuntimeItem();
			item.load((Element) child);
			node.addChild(item);
		}
		return node;
	}
}
