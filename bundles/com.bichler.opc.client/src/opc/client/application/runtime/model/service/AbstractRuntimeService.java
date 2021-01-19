package opc.client.application.runtime.model.service;

import opc.client.application.runtime.model.AbstractIdentifiedNode;

public abstract class AbstractRuntimeService implements IRuntimeService {
	private AbstractIdentifiedNode node;

	public AbstractRuntimeService() {
	}

	@Override
	public void setServiceNode(AbstractIdentifiedNode node) {
		this.node = node;
	}

	@Override
	public AbstractIdentifiedNode getServiceNode() {
		return this.node;
	}
}
