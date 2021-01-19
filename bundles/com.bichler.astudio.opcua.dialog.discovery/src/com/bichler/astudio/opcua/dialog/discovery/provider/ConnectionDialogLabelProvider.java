package com.bichler.astudio.opcua.dialog.discovery.provider;

import opc.client.application.UADiscoveryClient;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.progress.UIJob;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.dialog.discovery.DialogDiscoveryActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ConnectionDialogLabelProvider extends LabelProvider {

	// private Image serverUA = null;
	// private Image serverDiscovery = null;
	// private Image endpoint = null;
	// private Image securityNone = null;
	// private Image security = null;
	private Label errorMessage;

	private ConnectionDialogLabelProvider() {
		// this.serverUA = ResourceManager.getInstance().resolveImage(
		// IOPCClientImages.CONNECTIONIMAGEPATH,
		// IOPCClientImages.SERVER_UA);

		// this.serverDiscovery = ResourceManager.getInstance().resolveImage(
		// IOPCClientImages.CONNECTIONIMAGEPATH,
		// IOPCClientImages.SERVER_DISCOVERY_UA);

		// this.endpoint = ResourceManager.getInstance().resolveImage(
		// IOPCClientImages.CONNECTIONIMAGEPATH,
		// IOPCClientImages.SERVER_ENDPOINT);

		// this.securityNone = ResourceManager.getInstance().resolveImage(
		// IOPCClientImages.CONNECTIONIMAGEPATH,
		// IOPCClientImages.SERVER_SECURITY_NONE);
		//
		// this.security = ResourceManager.getInstance().resolveImage(
		// IOPCClientImages.CONNECTIONIMAGEPATH,
		// IOPCClientImages.SERVER_SECURITY);

	}

	public ConnectionDialogLabelProvider(Label errorMessages) {
		this();
		this.errorMessage = errorMessages;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ApplicationDescription) {
			return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
					CommonImagesActivator.SERVER_STANDBY);
			// return this.serverUA;
		} else if (element instanceof UADiscoveryClient) {
			return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
					CommonImagesActivator.SERVER_DISCOVERY);
			// return this.serverDiscovery;
		} else if (element instanceof String) {
			return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
					CommonImagesActivator.SERVER_ENDPOINT);
			// return this.endpoint;
		} else if (element instanceof EndpointDescription) {
			if (((EndpointDescription) element).getSecurityMode() == MessageSecurityMode.None) {
				return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
						CommonImagesActivator.SECURITY_NONE);
				// return this.securityNone;
			} else {
				return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
						CommonImagesActivator.SECURITY_SECURE);
				// return this.security;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ApplicationDescription) {
			String s = "";
			if (((ApplicationDescription) element).getApplicationName().getText() != null
					&& !((ApplicationDescription) element).getApplicationName().getText().isEmpty()) {
				s += ((ApplicationDescription) element).getApplicationName().getText();
				// + " || "
				// + ((ApplicationDescription) element).getApplicationUri()
				;
			}
			if (s.isEmpty()) {
				s += ((ApplicationDescription) element).getProductUri();
			}

			if (s.isEmpty() || "null".equals(s)) {
				s = ((ApplicationDescription) element).getApplicationUri();
			}

			return s;
		} else if (element instanceof String) {
			return (String) element;
		} else if (element instanceof EndpointDescription) {
			try {
				String s = SecurityPolicy.getSecurityPolicy(((EndpointDescription) element).getSecurityPolicyUri())
						.getPolicyUri().split("#")[1];
				return // ((EndpointDescription) element).getEndpointUrl() + " "
				/* + */s + " / " + ((EndpointDescription) element).getSecurityMode();
			} catch (ServiceResultException e) {
				if (errorMessage != null && !errorMessage.isDisposed()) {
					UIJob updateErrorMessage = new UIJob(Display.getDefault(), "UpdateErrorMessage") {

						@Override
						public IStatus runInUIThread(IProgressMonitor monitor) {
							if (errorMessage != null && !errorMessage.isDisposed()) {
								errorMessage.setText(
										CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE,
												"error.endpoints.nosecurity"));
							}
							return Status.OK_STATUS;
						}
					};
					updateErrorMessage.setUser(false);
					updateErrorMessage.schedule();
				}
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
		// if (this.endpoint != null)
		// this.endpoint.dispose();
		// if (this.security != null)
		// this.security.dispose();
		// if (this.securityNone != null)
		// this.securityNone.dispose();
		// if (this.serverDiscovery != null)
		// this.serverDiscovery.dispose();
		// if (this.serverUA != null)
		// this.serverUA.dispose();
	}

}
