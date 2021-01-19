package opc.client.application.runtime.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import opc.client.application.runtime.model.node.DatabaseRuntimeNode;
import opc.client.application.runtime.model.profile.AbstractProfileNode;
import opc.client.application.runtime.model.profile.DBProfileNode;
import opc.client.application.runtime.model.service.IRuntimeService;
import opc.client.util.DBConnector;

public abstract class RuntimeModelDriver extends AbstractRuntimeFactory {
	private Map<Integer, ServerNode> servers;
	private List<AbstractProfileNode> profiles;
	private Map<String, DBConnector> dbConnections = new HashMap<>();

	protected RuntimeModelDriver() {
		this.servers = new LinkedHashMap<>();
		this.profiles = new ArrayList<>();
	}

	public void addServer(ServerNode node) {
		this.servers.put(node.getId(), node);
	}

	public void addProfile(AbstractProfileNode node) {
		this.profiles.add(node);
	}

	public List<IRuntimeService> createModel() {
		List<IRuntimeService> requests = new ArrayList<>();
		for (ServerNode server : this.servers.values()) {
			doConnectionToServer(requests, server);
			createProfiles(requests, server);
		}
		return requests;
	}

	public DBConnector getDatabaseConnector(String drv, String database, String user, String password) {
		for (Entry<String, DBConnector> connector : this.dbConnections.entrySet()) {
			String driver = connector.getValue().getDrv();
			String connection = connector.getValue().getConnection();
			String usr = connector.getValue().getUser();
			String pwd = connector.getValue().getPassword();
			if (driver.equals(drv) && connection.equals(database) && usr.equals(user) && pwd.equals(password)) {
				return connector.getValue();
			}
		}
		return null;
	}

	void mapProfilesWithServers() {
		for (AbstractProfileNode node : this.profiles) {
			addNodeToServer(node);
		}
	}

	private void addNodeToServer(AbstractProfileNode node) {
		for (AbstractIdentifiedNode child : node.getChildren()) {
			int id = child.getId();
			ServerNode server = this.servers.get(id);
			if (server == null) {
				continue;
			}
			switch (node.getType()) {
			case DA:
				server.addDataAccess(child);
				break;
			case DC:
				server.addDataChange(child);
				break;
			case AC:
				server.addAlarm(child);
				break;
			case EVENTS:
				server.addEvent(child);
				break;
			case HA:
				server.addHistoryAccess(child);
				break;
			case PROGRAMS:
				server.addProgram(child);
				break;
			case DB:
				server.addDatabase(child);
				break;
			default:
				break;
			}
		}
	}

	public DBProfileNode[] getDatabaseNodes() {
		List<DBProfileNode> dbs = new ArrayList<>();
		for (AbstractProfileNode node : this.profiles) {
			if (node.getType() == ProjectsStoreConfiguration.DB)
				dbs.add((DBProfileNode) node);
		}
		return dbs.toArray(new DBProfileNode[0]);
	}

	public void initializeDatabaseConnections() {
		DBProfileNode[] nodes = getDatabaseNodes();
		for (DBProfileNode node : nodes) {
			List<AbstractIdentifiedNode> children = node.getChildren();
			for (AbstractIdentifiedNode child : children) {
				DatabaseRuntimeNode item = (DatabaseRuntimeNode) child;
				DBConnector connector = new DBConnector(item.getDatabaseDriver(), item.getDatabaseConnection(),
						item.getUser(), item.getPassword());
				if (this.dbConnections.containsKey(connector.toDbString())) {
					continue;
				}
				connector.initializeDBDriver();
				connector.connect();
				this.dbConnections.put(connector.toDbString(), connector);
			}
		}
	}
}
