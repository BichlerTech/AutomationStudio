package com.bichler.astudio.opcua.nodes.security;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.transport.security.KeyPair;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.handlers.security.OpenCertificateEditorHandler;

public class OPCUAKeyPairModelNode extends AbstractOPCUACertificateModelNode {

	private KeyPair keyPair = null;
	private String certificatePath;
	private String privateKeyPath;

	@Override
	public void nodeDBLClicked() {
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(ICommandService.class);

		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getService(IHandlerService.class);

		Command openConfigCmd = commandService.getCommand(OpenCertificateEditorHandler.ID);

		ExecutionEvent executionOpenConfigEvent = handlerService.createExecutionEvent(openConfigCmd, null);

		try {
			openConfigCmd.executeWithChecks(executionOpenConfigEvent);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Image getLabelImage() {
		return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.CERTIFICATE);
		// return StudioImageActivator
		// .getImage(StudioImages.ICON_OPCUACERTIFICATE);
	}

	@Override
	public String getLabelText() {
		return this.keyPair.getCertificate().getCertificate().getSubjectX500Principal().getName();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();

		if (nodes == null) {
			nodes = new ArrayList<>();
		}

		return nodes.toArray();
	}

	@Override
	public void refresh() {

	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setCertificatePath(String path) {
		this.certificatePath = path;
	}

	public void setKeyPair(KeyPair certificate) {
		this.keyPair = certificate;
	}

	public void setPrivateKeyPath(String path) {
		this.privateKeyPath = path;
	}

	@Override
	public X509Certificate getCertificate() {
		return this.keyPair.getCertificate().certificate;
	}
}
