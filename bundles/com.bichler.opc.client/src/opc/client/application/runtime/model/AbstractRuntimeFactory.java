package opc.client.application.runtime.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.TimestampsToReturn;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;
import opc.client.application.runtime.model.node.AbstractMonitorRuntimeItem;
import opc.client.application.runtime.model.node.AbstractSubscriptionNode;
import opc.client.application.runtime.model.service.IRuntimeService;
import opc.client.application.runtime.model.service.MonitoredItemCreate;
import opc.client.application.runtime.model.service.SessionActivate;
import opc.client.application.runtime.model.service.SessionCreate;
import opc.client.application.runtime.model.service.SubscriptionCreate;
import opc.client.service.ClientSession;
import opc.client.service.MonitoredItem;
import opc.client.service.Subscription;

public abstract class AbstractRuntimeFactory {
	public void executeModel(UAClient client, List<IRuntimeService> model) {
		ClientSession session = null;
		Subscription subscription = null;
		for (IRuntimeService service : model) {
			try {
				if (service instanceof SessionCreate) {
					session = execSessionCreate(client, (SessionCreate) service);
				} else if (service instanceof SessionActivate) {
					execSessionActivate(client, session);
				} else if (service instanceof SubscriptionCreate) {
					subscription = execSubscriptionCreate(client, (SubscriptionCreate) service);
				} else if (service instanceof MonitoredItemCreate && subscription != null) {
					execMonitoredItemCreate(client, subscription, (MonitoredItemCreate) service);
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	private void execSessionActivate(UAClient client, ClientSession session) throws ServiceResultException {
		client.activateSession(session, null);
	}

	private ClientSession execSessionCreate(UAClient client, SessionCreate service) throws ServiceResultException {
		UADiscoveryClient discovery = new UAClient().createDiscoveryClient();
		EndpointDescription[] endpoints = discovery.getEndpoints(false, service.getEndpointUrl());
		EndpointDescription endpoint = discovery.selectEndpointNone(endpoints);
		return client.createSession(endpoint, service.getSessionName());
	}

	private MonitoredItem execMonitoredItemCreate(UAClient client, Subscription subscription,
			MonitoredItemCreate service) throws ServiceResultException {
		MonitoredItem item = client.createMonitoredItem(null, subscription.getSubscriptionId(), service.getNodeId(),
				service.getAttributeId(), null, null, service.getInterval(), null, service.getQueueSize(), true,
				service.getMonitoringMode(), TimestampsToReturn.Both, null);
		registerMonitoredItemWithListener(item, service.getServiceNode());
		return item;
	}

	private Subscription execSubscriptionCreate(UAClient client, SubscriptionCreate service)
			throws ServiceResultException {
		Subscription subscription = client.createSubscription(null, service.getInterval(), service.isEnable(), 1000, 0,
				service.getLifetime(), service.getKeepalive());
		subscription.setOnlyValidTimestampPublishValuesAllowed(false);
		return subscription;
	}

	void doConnectionToServer(List<IRuntimeService> requests, ServerNode server) {
		addSessionCreateRequest(requests, server);
		addSessionCreateActivate(requests, server);
	}

	void createProfiles(List<IRuntimeService> requests, ServerNode server) {
		createProfileDatabase(requests, server);
	}

	private void createDatabase(List<IRuntimeService> requests, AbstractSubscriptionNode database) {
		if (database.getChildren().isEmpty()) {
			return;
		}
		addSubscriptionCreateRequest(requests, database);
		for (AbstractMonitorRuntimeItem child : database.getChildren()) {
			AbstractMonitorRuntimeItem item = child;
			addMonitoredItemCreateRequest(requests, item);
		}
	}

	private void createProfileDatabase(List<IRuntimeService> requests, ServerNode server) {
		List<AbstractIdentifiedNode> databases = server.getDatabaseProfiles();
		for (AbstractIdentifiedNode database : databases) {
			createDatabase(requests, (AbstractSubscriptionNode) database);
		}
	}

	private void addMonitoredItemCreateRequest(List<IRuntimeService> requests, AbstractMonitorRuntimeItem node) {
		MonitoredItemCreate request = new MonitoredItemCreate();
		request.setNodeId(node.getNodeId());
		request.setInterval(node.getInterval());
		request.setMonitoringMode(node.getMode());
		request.setNodeClass(node.getNodeClass());
		request.setQueueSize(node.getQueueSize());
		request.setAttributeId(node.getAttributeId());
		request.setServiceNode(node);
		requests.add(request);
	}

	private void addSessionCreateActivate(List<IRuntimeService> requests, ServerNode node) {
		SessionActivate request = new SessionActivate();
		request.setServiceNode(node);
		requests.add(request);
	}

	private void addSessionCreateRequest(List<IRuntimeService> requests, ServerNode node) {
		SessionCreate request = new SessionCreate();
		request.setEndpointUrl(node.getEndpointUrl());
		request.setSessionName(node.getSessionName());
		request.setServiceNode(node);
		requests.add(request);
	}

	private void addSubscriptionCreateRequest(List<IRuntimeService> requests, AbstractSubscriptionNode node) {
		SubscriptionCreate request = new SubscriptionCreate();
		request.setEnable(node.isEnable());
		request.setInterval(node.getInterval());
		request.setKeepalive(node.getKeepalive());
		request.setLifetime(node.getLifetime());
		request.setServiceNode(node);
		requests.add(request);
	}

	public abstract void registerMonitoredItemWithListener(MonitoredItem item,
			AbstractIdentifiedNode abstractIdentifiedNode);
}
