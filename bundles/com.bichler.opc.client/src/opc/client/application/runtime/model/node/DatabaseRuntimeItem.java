package opc.client.application.runtime.model.node;

import opc.client.application.runtime.model.ProjectsStoreConfiguration;

import org.jdom.Element;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;

public class DatabaseRuntimeItem extends AbstractMonitorRuntimeItem {
	private String tablename;

	public DatabaseRuntimeItem() {
		super();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrTable = element.getAttributeValue(ProjectsStoreConfiguration.TABLE.name());
		setTablename(attrTable);
	}

	public String getTablename() {
		return this.tablename;
	}

	private void setTablename(String table) {
		this.tablename = table;
	}

	@Override
	public UnsignedInteger getAttributeId() {
		return Attributes.Value;
	}
}
