package opc.client.application.runtime.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Element;

public abstract class AbstractIdentifiedNode implements IModelNode {
	private int id = -1;

	public AbstractIdentifiedNode() {
		super();
	}

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	@Override
	public void load(Element element) {
		try {
			String attrId = element.getAttributeValue(ProjectsStoreConfiguration.ID.name());
			setId(Integer.parseInt(attrId));
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}
}
