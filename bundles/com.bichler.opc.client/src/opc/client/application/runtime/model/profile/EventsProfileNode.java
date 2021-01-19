package opc.client.application.runtime.model.profile;

import java.util.List;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.EventsRuntimeItem;
import opc.client.application.runtime.model.node.EventsRuntimeNode;

import org.jdom.Element;

public class EventsProfileNode extends AbstractProfileNode {
	EventsProfileNode() {
		super(ProjectsStoreConfiguration.EVENTS);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		EventsRuntimeNode node = new EventsRuntimeNode();
		node.load(element);
		List<?> children = element.getChildren();
		for (Object child : children) {
			EventsRuntimeItem item = new EventsRuntimeItem();
			item.load((Element) child);
			node.addChild(item);
		}
		return node;
	}
}
