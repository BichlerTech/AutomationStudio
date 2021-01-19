package com.bichler.astudio.opcua.nodes.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.handlers.OpenOPCUASecurityConfigurationHandler;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class OPCUAServerSecurityModelNode extends OPCUAServerModelNode {
	@Override
	public void nodeDBLClicked() {
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);
		Command openConfigCmd = commandService.getCommand(OpenOPCUASecurityConfigurationHandler.ID);
		ExecutionEvent executionOpenConfigEvent = handlerService.createExecutionEvent(openConfigCmd, new Event());
		try {
			openConfigCmd.executeWithChecks(executionOpenConfigEvent);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getLabelImage() {
		return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.KEY);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		String serverName = this.getServerName();
		if (nodes == null) {
			nodes = new ArrayList<>();
			String path = null;
			// server cert path
			try {
				path = new Path(filesystem.getRootPath()).append("certificatestore").append("certs").toOSString();
				if (!filesystem.isDir(path)) {
					filesystem.addDir(path);
				}
				AbstractOPCUAServerCertificateStoreModelNode certType = new OPCUAServerCertificatesModelNode();
				certType.setServerName(serverName);
				certType.setFilesystem(this.filesystem);
				certType.setParent(this);
				certType.setCertTypePath("certs");
				certType.setName(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "node.servercert"));
				nodes.add(certType);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// rejected cert path
			try {
				path = new Path(filesystem.getRootPath()).append("certificatestore").append("rejected").toOSString();
				if (!filesystem.isDir(path)) {
					filesystem.addDir(path);
				}
				AbstractOPCUAServerCertificateStoreModelNode rejectedType = new OPCUAServerRejectedStoreModelNode();
				rejectedType.setServerName(serverName);
				rejectedType.setFilesystem(this.filesystem);
				rejectedType.setParent(this);
				rejectedType.setCertTypePath("rejected");
				rejectedType.setName(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "node.rejectcert"));
				nodes.add(rejectedType);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// rejected cert path
			try {
				path = new Path(filesystem.getRootPath()).append("certificatestore").append("trusted").toOSString();
				if (!filesystem.isDir(path)) {
					filesystem.addDir(path);
				}
				AbstractOPCUAServerCertificateStoreModelNode trustedType = new OPCUAServerTrustedStoreModelNode();
				trustedType.setServerName(serverName);
				trustedType.setFilesystem(this.filesystem);
				trustedType.setParent(this);
				trustedType.setCertTypePath("trusted");
				trustedType.setName(
						CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "node.trustedcert"));
				nodes.add(trustedType);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return nodes.toArray();
	}
}
