package opc.client.application.runtime.model.node;

import opc.client.application.runtime.model.ProjectsStoreConfiguration;

import org.jdom.Element;

public class DataAccessRuntimeItem extends AbstractRuntimeItem {
	private String timestampToReturn = "";
	private double maxAge = -1;

	public DataAccessRuntimeItem() {
		super();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrTimestampToReturn = element.getAttributeValue(ProjectsStoreConfiguration.TIMESTAMPTORETURN.name());
		setTimestampToReturn(attrTimestampToReturn);
		String attrMaxAge = element.getAttributeValue(ProjectsStoreConfiguration.MAXAGE.name());
		setMaxAge(Double.parseDouble(attrMaxAge));
	}

	public String getTimestampToReturn() {
		return timestampToReturn;
	}

	public double getMaxAge() {
		return maxAge;
	}

	private void setTimestampToReturn(String timestampToReturn) {
		this.timestampToReturn = timestampToReturn;
	}

	private void setMaxAge(double maxAge) {
		this.maxAge = maxAge;
	}
}
