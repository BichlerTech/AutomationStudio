package com.bichler.astudio.opcua.handlers.security;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.transport.security.KeyPair;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.security.wizard.util.CertGenUtil;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.nodes.security.OPCUAKeyPairModelNode;

public class RenewCertificateHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.commands.certificate.renew";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		if (page == null) {
			return null;
		}

		TreeSelection selection = (TreeSelection) page.getSelection(OPCNavigationView.ID);

		if (selection == null) {
			return null;
		}

		OPCUAKeyPairModelNode selectedNode = (OPCUAKeyPairModelNode) selection.getFirstElement();

		// if(event.getTrigger() instanceof CometOPCUAEcmaScriptOpenEvent) {
		KeyPair keyPair = selectedNode.getKeyPair();

		// try {
		// String applicationUri = CertificateUtils
		// .getApplicationUriOfCertificate(keyPair.getCertificate());
		// String saname = keyPair.getCertificate().getCertificate()
		// .getSigAlgName();
		// X500Principal subjectPrincipals = keyPair.getCertificate()
		// .getCertificate().getSubjectX500Principal();
		// String spName = subjectPrincipals.getName();
		// Principal issuerDN =
		// keyPair.getCertificate().getCertificate().getIssuerDN();
		// Date after = keyPair.getCertificate().getCertificate().getNotAfter();
		// Date before =
		// keyPair.getCertificate().getCertificate().getNotBefore();
		// } catch (CertificateParsingException e) {
		// e.printStackTrace();
		// }

		File certFile = new File(selectedNode.getCertificatePath());
		File keyFile = new File(selectedNode.getPrivateKeyPath());

		KeyPair renew = CertGenUtil.openRenewWizard(keyPair, certFile, keyFile);
		CertGenUtil.store(renew, certFile, keyFile);

		// refresh
		StudioModelNode parent = selectedNode.getParent();
		OPCNavigationView navigation = (OPCNavigationView) page.findView(OPCNavigationView.ID);

		if (navigation == null) {
			return null;
		}

		navigation.refresh(parent);

		// CertificateUtils.renewApplicationInstanceCertificate(commonName,
		// organisation, applicationUri, validityTime, keyPair, hostNames);
		return null;
	}

}
