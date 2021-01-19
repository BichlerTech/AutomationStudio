package opc.client.application.runtime.model.service;

public class SessionCreate extends AbstractRuntimeService {
	private String endpointUrl;
	private String sessionName;

	public SessionCreate() {
		// constructor to generate a default session create object
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
