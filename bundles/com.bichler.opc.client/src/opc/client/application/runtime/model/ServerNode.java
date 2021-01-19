package opc.client.application.runtime.model;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class ServerNode extends AbstractIdentifiedNode {
	private List<AbstractIdentifiedNode> dataaccess;
	private List<AbstractIdentifiedNode> historyaccess;
	private List<AbstractIdentifiedNode> program;
	private List<AbstractIdentifiedNode> datachange;
	private List<AbstractIdentifiedNode> database;
	private List<AbstractIdentifiedNode> alarms;
	private List<AbstractIdentifiedNode> events;
	private String endpointUrl = "";
	private String sessionName = "";

	protected ServerNode() {
		super();
		this.dataaccess = new ArrayList<>();
		this.historyaccess = new ArrayList<>();
		this.program = new ArrayList<>();
		this.datachange = new ArrayList<>();
		this.database = new ArrayList<>();
		this.alarms = new ArrayList<>();
		this.events = new ArrayList<>();
	}

	@Override
	public void load(Element element) {
		super.load(element);
		String attrEndpointUrl = element.getAttributeValue(ProjectsStoreConfiguration.SERVERURL.name());
		setEndpointUrl(attrEndpointUrl);
		String attrSessionname = element.getAttributeValue(ProjectsStoreConfiguration.SESSIONNAME.name());
		setSessionName(attrSessionname);
	}

	public static ServerNode parse(Element element) {
		ServerNode node = new ServerNode();
		node.load(element);
		return node;
	}

	public void addDataAccess(AbstractIdentifiedNode daItem) {
		this.dataaccess.add(daItem);
	}

	public void addHistoryAccess(AbstractIdentifiedNode child) {
		this.historyaccess.add(child);
	}

	public void addProgram(AbstractIdentifiedNode child) {
		this.program.add(child);
	}

	public void addDataChange(AbstractIdentifiedNode child) {
		this.datachange.add(child);
	}

	public void addAlarm(AbstractIdentifiedNode child) {
		this.alarms.add(child);
	}

	public void addEvent(AbstractIdentifiedNode child) {
		this.events.add(child);
	}

	public void addDatabase(AbstractIdentifiedNode child) {
		this.database.add(child);
	}

	public List<AbstractIdentifiedNode> getDatabaseProfiles() {
		return this.database;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	private void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getSessionName() {
		return sessionName;
	}

	private void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
