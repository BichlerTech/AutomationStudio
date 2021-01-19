package com.bichler.astudio.opcua.security.wizard.util;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.bichler.astudio.opcua.security.Activator;
import com.bichler.astudio.opcua.security.wizard.RenewCertificateWizard;
import com.bichler.astudio.utils.internationalization.CustomString;

public class CertGenUtil {

	public static KeyPair openRenewWizard(KeyPair key2renew, File cert, File key) {
		// Cert certificate = Cert.load(cert);
		// PrivKey privKey = PrivKey.load(key);
		//
		KeyPair renew = openRenewWizard(key2renew);

		// store(renew, cert, key);

		return renew;
	}

	public static void store(KeyPair renew, File certFile, File keyFile) {
		// TODO: stores keypair to file
		if (renew == null) {
			// cancel wizard
			return;
		}

		if (certFile == null || keyFile == null) {
			// do not store
			MessageDialog.openWarning(Display.getDefault().getActiveShell(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.error.save"),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"wizard.renew.error.save.description"));
			return;
		}

		Cert cert = renew.getCertificate();
		PrivKey key = renew.getPrivateKey();

		try {
			cert.save(certFile);
			key.save(keyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static KeyPair openRenewWizard(KeyPair key) {
		RenewCertificateWizard wizard = new RenewCertificateWizard(key);
		WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);

		int open = dialog.open();
		if (WizardDialog.OK == open) {
			// renew
			String commonName = wizard.getCN(), organisation = wizard.getOrganisation(),
					applicationUri = wizard.getApplicationUri();

			int validityTime = wizard.getValidDays();

			KeyPair oldKeys = key;
			// KeyPair issuerKeys = null;

			try {

				return CertificateUtils.renewApplicationInstanceCertificate(commonName, organisation, applicationUri,
						validityTime, oldKeys, null, new String[0]);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static final String ATTR_CN = "CN=";
	public static final String ATTR_O = "O=";

	public static String getValByAttributeTypeFromIssuerDN(Cert cert, String attributeType) {

		String dn = cert.getCertificate().getSubjectX500Principal().getName();

		String[] dnSplits = dn.split(",");
		for (String dnSplit : dnSplits) {
			if (dnSplit.contains(attributeType)) {
				String[] cnSplits = dnSplit.trim().split("=");
				if (cnSplits[1] != null) {
					return cnSplits[1].trim();
				}
			}
		}
		return "";
	}

}
