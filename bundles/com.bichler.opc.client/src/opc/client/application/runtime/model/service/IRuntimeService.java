package opc.client.application.runtime.model.service;

import opc.client.application.runtime.model.AbstractIdentifiedNode;

public interface IRuntimeService {
	public void setServiceNode(AbstractIdentifiedNode node);

	public AbstractIdentifiedNode getServiceNode();
}
