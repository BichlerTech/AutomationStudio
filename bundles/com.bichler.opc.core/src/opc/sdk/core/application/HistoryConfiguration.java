package opc.sdk.core.application;

import org.jdom.Content;
import org.jdom.Element;

public class HistoryConfiguration {
	private boolean isActive = false;
	private String drvName = "";
	private String databaseName = "";
	private String database = "";
	private String user = "";
	private String pw = "";
	private boolean plainValue = true;

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public void setActive(Element active) {
		if (active.getContentSize() == 0) {
			this.isActive = false;
			return;
		}
		this.isActive = Boolean
				.parseBoolean(((Content) (active.getContent().get(active.getContentSize() - 1))).getValue());
	}

	public String getDrvName() {
		return drvName;
	}

	public void setDrvName(Element drvName) {
		if (drvName.getContentSize() == 0) {
			this.drvName = "";
			return;
		}
		this.drvName = ((Content) (drvName.getContent().get(drvName.getContentSize() - 1))).getValue();
	}

	public void setDrvName(String drvName) {
		this.drvName = drvName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(Element databaseUrl) {
		if (databaseUrl.getContentSize() == 0) {
			this.databaseName = "";
			return;
		}
		this.databaseName = ((Content) (databaseUrl.getContent().get(databaseUrl.getContentSize() - 1))).getValue();
	}

	public void setDatabaseName(String databaseUrl) {
		this.databaseName = databaseUrl;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(Element database) {
		if (database.getContentSize() == 0) {
			this.database = "";
			return;
		}
		this.database = ((Content) (database.getContent().get(database.getContentSize() - 1))).getValue();
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(Element user) {
		if (user.getContentSize() == 0) {
			this.user = "";
			return;
		}
		this.user = ((Content) (user.getContent().get(user.getContentSize() - 1))).getValue();
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(Element pw) {
		if (pw.getContentSize() == 0) {
			this.pw = "";
			return;
		}
		this.pw = ((Content) (pw.getContent().get(pw.getContentSize() - 1))).getValue();
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public boolean isPlainValue() {
		return plainValue;
	}

	public void setPlainValue(boolean plainValue) {
		this.plainValue = plainValue;
	}
}
