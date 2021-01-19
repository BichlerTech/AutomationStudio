package com.bichler.astudio.opcua.components.ui.dialogs.providers;

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

import com.bichler.astudio.components.ui.ComponentsUIActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.components.ui.ComponentsSharedImages;

public class OPCUAConnectionDialogLabelProvider extends LabelProvider {

	private Image serverUA = null;
	private Image serverDiscovery = null;
	private Image endpoint = null;
	private Image securityNone = null;
	private Image security = null;
	private Label errorMessage;

	private OPCUAConnectionDialogLabelProvider() {
		
		this.serverUA = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_SERVER_UA);

		this.serverDiscovery = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_DISCOVERY_UA);

		this.endpoint = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_ENDPOINT_UA);

		this.securityNone = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_SECURITY_NONE);

		this.security = ComponentsSharedImages.getImage(ComponentsSharedImages.ICON_SECURITY_UA);

	}

	public OPCUAConnectionDialogLabelProvider(Label errorMessages) {
		this();
		this.errorMessage = errorMessages;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ApplicationDescription) {
			return this.serverUA;
		} else if (element instanceof UADiscoveryClient) {
			return this.serverDiscovery;
		} else if (element instanceof String) {
			return this.endpoint;
		} else if (element instanceof EndpointDescription) {
			if (((EndpointDescription) element).getSecurityMode() == MessageSecurityMode.None) {
				return this.securityNone;
			} else {
				return this.security;
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ApplicationDescription) {
			String s = "";
			if (((ApplicationDescription) element).getApplicationName()
					.getText() != null
					&& !((ApplicationDescription) element).getApplicationName()
							.getText().isEmpty()) {
				s += ((ApplicationDescription) element).getApplicationName()
						.getText();
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
				String s = SecurityPolicy
						.getSecurityPolicy(
								((EndpointDescription) element)
										.getSecurityPolicyUri()).getPolicyUri()
						.split("#")[1];
				return // ((EndpointDescription) element).getEndpointUrl() + " "
				/* + */s + " / "
						+ ((EndpointDescription) element).getSecurityMode();
			} catch (ServiceResultException e) {
				if (errorMessage != null && !errorMessage.isDisposed()) {
					UIJob updateErrorMessage = new UIJob(Display.getDefault(),
							"UpdateErrorMessage") {

						@Override
						public IStatus runInUIThread(IProgressMonitor monitor) {
							if (errorMessage != null
									&& !errorMessage.isDisposed()) {
								errorMessage
										.setText(CustomString
												.getString(ComponentsUIActivator.getDefault().RESOURCE_BUNDLE,"ERROR.DISCOVERY.SECURITYPOLICY"));
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
	}

}
