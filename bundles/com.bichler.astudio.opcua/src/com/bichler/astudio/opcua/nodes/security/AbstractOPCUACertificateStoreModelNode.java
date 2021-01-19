package com.bichler.astudio.opcua.nodes.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.transport.security.Cert;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class AbstractOPCUACertificateStoreModelNode extends AbstractOPCUAServerCertificateStoreModelNode {

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();

		if (nodes == null) {
			nodes = new ArrayList<>();
			// storepath
			String store = getCertStorePath();
			String path = new Path(this.filesystem.getRootPath()).append("certificatestore").append(store).toOSString();

			try {
				String[] files = this.filesystem.listFiles(path);

				if (files != null && files.length > 0) {

					for (String file : files) {
						String certificatePath = new Path(path).append(file).toOSString();

//						InputStream in = null;
						try {
//							in = this.filesystem.readFile(certificatePath);
//							Cert cert = Cert.load(in);
							Cert cert = Cert.load(new File(certificatePath));
							OPCUACertificateModelNode node = new OPCUACertificateModelNode();
							node.setFilesystem(this.filesystem);
							node.setParent(this);

							node.setCertificate(cert.getCertificate());
							node.setServerName(getServerName());
							node.setCertPath(certificatePath);
							nodes.add(node);
						} catch (CertificateEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (CertificateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
//						} finally {
//							if (in != null) {
//								try {
//									in.close();
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//							}
						}

					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return nodes.toArray();
	}
}
