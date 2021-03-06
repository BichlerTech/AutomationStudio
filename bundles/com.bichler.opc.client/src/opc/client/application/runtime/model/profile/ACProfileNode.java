package opc.client.application.runtime.model.profile;

import java.util.List;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.AlarmsRuntimeItem;
import opc.client.application.runtime.model.node.AlarmsRuntimeNode;

import org.jdom.Element;

public class ACProfileNode extends AbstractProfileNode {
	ACProfileNode() {
		super(ProjectsStoreConfiguration.AC);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		AlarmsRuntimeNode node = new AlarmsRuntimeNode();
		node.load(element);
		List<?> children = element.getChildren();
		for (Object child : children) {
			AlarmsRuntimeItem item = new AlarmsRuntimeItem();
			item.load((Element) child);
			node.addChild(item);
		}
		return node;
	}
}
