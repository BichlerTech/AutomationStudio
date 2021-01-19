package com.bichler.astudio.opcua.handlers.security;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.transport.security.Cert;

import com.bichler.astudio.filesystem.IFileSystem;

public class SecurityUtil {

	public static void importCertificateToCertificateStore(Shell shell, IFileSystem filesystem, String store) {

		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setFilterExtensions(new String[] { "*.der" });

		String open = dialog.open();

		// dialog has been cancled
		if (open == null) {
			return;
		}
		String path = dialog.getFilterPath();
		String[] paths = dialog.getFileNames();

		for (String element : paths) {
			// certificate
			Cert cert = null;
//			InputStream in = null;
			try {
				// read certificate from file
				String pathCert = new Path(path).append(element).toOSString();
				if (!filesystem.isFile(pathCert)) {
					continue;
				}

//				in = filesystem.readFile(pathCert);
				// load
//				cert = Cert.load(in);
				cert = Cert.load(new File(pathCert));
			} catch (CertificateEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// skip on error
			if (cert == null) {
				continue;
			}
			// store in certificate store
			String file = new Path(filesystem.getRootPath()).append("certificatestore").append(store).append(element)
					.toString();
			try {
				if (!filesystem.isFile(file)) {
					filesystem.addFile(file);
				}
				cert.save(new File(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
