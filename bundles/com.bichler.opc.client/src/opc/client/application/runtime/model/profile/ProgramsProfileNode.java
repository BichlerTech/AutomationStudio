package opc.client.application.runtime.model.profile;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;
import opc.client.application.runtime.model.node.ProgramsRuntimeItem;

import org.jdom.Element;

public class ProgramsProfileNode extends AbstractProfileNode {
	ProgramsProfileNode() {
		super(ProjectsStoreConfiguration.DC);
	}

	@Override
	AbstractIdentifiedNode createChild(Element element) {
		ProgramsRuntimeItem node = new ProgramsRuntimeItem();
		node.load(element);
		addChild(node);
		return node;
	}
}
