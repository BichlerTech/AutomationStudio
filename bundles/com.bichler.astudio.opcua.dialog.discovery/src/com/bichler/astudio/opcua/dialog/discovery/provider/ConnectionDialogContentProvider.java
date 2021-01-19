package com.bichler.astudio.opcua.dialog.discovery.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Label;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.transport.UriUtil;

import com.bichler.astudio.opcua.dialog.discovery.DialogDiscoveryActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ConnectionDialogContentProvider implements ITreeContentProvider {

	private boolean alwaysIP;
	private String discoveryURI;
	private Label errorMessage;
	private boolean alwaysValid;

	public ConnectionDialogContentProvider(Label errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ApplicationDescription) {
			UADiscoveryClient discoveryClient = new UAClient().createDiscoveryClient();
			Map<String, EndpointDescription[]> discoveryResults = null;
			String[] discUris = null;

			List<String> urls = new ArrayList<String>();
			discUris = ((ApplicationDescription) parentElement).getDiscoveryUrls();

			for (String uri : discUris) {
				if (alwaysIP) {
					try {
						URI discoveryURIToMAP = new URI(this.discoveryURI);
						URI endpointURI = new URI(uri);

						uri = discoveryURIToMAP.toASCIIString().replaceFirst(discoveryURIToMAP.getHost(),
								endpointURI.getHost());
					} catch (URISyntaxException e) {

					}
				}

				try {
					discoveryResults = discoveryClient.getEndpoints(alwaysValid, new String[] { uri });

					for (String url : discoveryResults.keySet()) {
						urls.add(url);
					}
				} catch (ServiceResultException e) {
					if (e instanceof ServiceFaultException) {
						urls.add(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE,
								"error.endpoints.error") + " "
								+ ((ServiceFaultException) e).getServiceFault().getResponseHeader().getServiceResult()
										.getName());
					}
				}
			}
			if (urls.isEmpty()) {
				if (!errorMessage.isDisposed()) {
					errorMessage.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							errorMessage.setText(CustomString.getString(
									DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "error.endpoints.noresult"));
							errorMessage.setVisible(true);
						}
					});

				}
			}
			return urls.toArray(new String[urls.size()]);
		} else if (parentElement instanceof String) {
			UADiscoveryClient discoveryClient = new UAClient().createDiscoveryClient();
			EndpointDescription[] discoveryResult = null;
			try {
				discoveryResult = discoveryClient.getEndpoints(alwaysValid, (String) parentElement);
			} catch (ServiceResultException e) {

			}
			return discoveryResult;
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ApplicationDescription) {
			return (((ApplicationDescription) element).getDiscoveryUrls() != null
					&& ((ApplicationDescription) element).getDiscoveryUrls().length > 0);
		} else if (element instanceof String) {
			try {
				if (element == null || ((String) element).isEmpty()) {
					return false;
				}
				UriUtil.getTransportProtocol(((String) element));
				UriUtil.getSocketAddress(((String) element));
			} catch (ServiceResultException e) {
				return false;
			}

			UADiscoveryClient discoveryClient = new UAClient().createDiscoveryClient();
			EndpointDescription[] discoveryResult = null;
			try {
				discoveryResult = discoveryClient.getEndpoints(alwaysValid, (String) element);
			} catch (ServiceResultException e) {

			}
			return discoveryResult != null && discoveryResult.length > 0;
		}

		return false;
	}

	public void setAlwaysIP(boolean alwaysIP) {
		this.alwaysIP = alwaysIP;
	}

	public void setAlwaysValid(boolean alwaysValid) {
		this.alwaysValid = alwaysValid;
	}

	public void setDiscoveryURL(String discoverURI) {
		this.discoveryURI = discoverURI;

	}

}
