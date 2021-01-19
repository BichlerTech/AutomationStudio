package opc.client.application.runtime.model.node;

import opc.client.application.runtime.model.ProjectsStoreConfiguration;

import org.jdom.Element;

public class DatabaseRuntimeNode extends AbstractSubscriptionNode {
	private String dbDriver = "";
	private String dbConnection = "";
	private String dbUser = "";
	private String dbPassword = "";

	public DatabaseRuntimeNode() {
		super();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrDBDrv = element.getAttributeValue(ProjectsStoreConfiguration.DRV.name());
		setDbDriver(attrDBDrv);
		String attrConnection = element.getAttributeValue(ProjectsStoreConfiguration.CONNECTION.name());
		setDbConnection(attrConnection);
		String attrDBUser = element.getAttributeValue(ProjectsStoreConfiguration.USER.name());
		setDbUser(attrDBUser);
		String attrDBPassword = element.getAttributeValue(ProjectsStoreConfiguration.PASSWORD.name());
		setDbPassword(attrDBPassword);
	}

	public String getDatabaseDriver() {
		return this.dbDriver;
	}

	public String getDatabaseConnection() {
		return this.dbConnection;
	}

	public String getUser() {
		return this.dbUser;
	}

	public String getPassword() {
		return this.dbPassword;
	}

	private void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	private void setDbConnection(String dbConnection) {
		this.dbConnection = dbConnection;
	}

	private void setDbUser(String user) {
		this.dbUser = user;
	}

	private void setDbPassword(String password) {
		this.dbPassword = password;
	}
}
