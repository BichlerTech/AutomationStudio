package opc.client.application.runtime.model.profile;

import java.util.ArrayList;
import java.util.List;

import opc.client.application.runtime.model.AbstractIdentifiedNode;
import opc.client.application.runtime.model.IModelNode;
import opc.client.application.runtime.model.ProjectsStoreConfiguration;

import org.jdom.Element;

public abstract class AbstractProfileNode implements IModelNode {
	private ProjectsStoreConfiguration type;
	private List<AbstractIdentifiedNode> children = new ArrayList<>();

	/**
	 * Has no ID
	 */
	protected AbstractProfileNode(ProjectsStoreConfiguration type) {
		super();
		this.type = type;
	}

	protected void addChild(AbstractIdentifiedNode node) {
		this.children.add(node);
	}

	/**
	 * Imports children for profile
	 * 
	 * @see opc.client.application.runtime.model.node.AbstractSubscriptionNode#load(org.jdom
	 *      .Element)
	 */
	@Override
	public void load(Element element) {
		List<?> tmpchildren = element.getChildren();
		for (Object child : tmpchildren) {
			AbstractIdentifiedNode node = createChild((Element) child);
			addChild(node);
		}
	}

	public List<AbstractIdentifiedNode> getChildren() {
		return this.children;
	}

	public ProjectsStoreConfiguration getType() {
		return this.type;
	}

	public static AbstractProfileNode parse(Element element) {
		String profile = element.getName();
		ProjectsStoreConfiguration type = ProjectsStoreConfiguration.valueOf(profile);
		AbstractProfileNode node;
		switch (type) {
		case AC:
			node = new ACProfileNode();
			break;
		case EVENTS:
			node = new EventsProfileNode();
			break;
		case DA:
			node = new DAProfileNode();
			break;
		case DC:
			node = new DCProfileNode();
			break;
		case HA:
			node = new HAProfileNode();
			break;
		case PROGRAMS:
			node = new ProgramsProfileNode();
			break;
		case DB:
			node = new DBProfileNode();
			break;
		default:
			throw new IllegalArgumentException("no profile available");
		}
		node.load(element);
		return node;
	}

	abstract AbstractIdentifiedNode createChild(Element element);
}
