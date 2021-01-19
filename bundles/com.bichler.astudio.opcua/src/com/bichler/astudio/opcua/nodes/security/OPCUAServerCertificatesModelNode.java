package com.bichler.astudio.opcua.nodes.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUAServerCertificatesModelNode extends AbstractOPCUAServerCertificateStoreModelNode {

	public OPCUAServerCertificatesModelNode() {
		super();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();

		if (nodes == null) {
			nodes = new ArrayList<>();
			// storepath
			String store = getCertStorePath();
			IPath path = new Path(this.filesystem.getRootPath()).append("certificatestore").append(store);

			IPath path_cert = path.append("publiccert");
			IPath path_key = path.append("privatekey");

			String[] certificateFiles = new String[0];
			try {
				certificateFiles = this.filesystem.listFiles(path_cert.toOSString());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String[] keyFiles = new String[0];
			try {
				keyFiles = this.filesystem.listFiles(path_key.toString());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			Map<String, String> keyNames = new HashMap<>();
			for (String name : keyFiles) {
				boolean valid = checkFile(name, "pfx", "pem");
				if (!valid) {
					continue;
				}

				String key = extractName(name, "_key");
				keyNames.put(key, name);
			}

			if (certificateFiles != null && certificateFiles.length > 0) {
				for (String file : certificateFiles) {

					boolean valid = checkFile(file, "der");
					if (!valid) {
						continue;
					}

					String key = extractName(file, "_cert");
					String keyname = keyNames.get(key);
					if (keyname == null) {
						// no key found
						continue;
					}

					String c = path_cert.append(file).toOSString();
					String k = path_key.append(keyname).toOSString();

					// read cert
					Cert cert = null;
//					InputStream in = null;
					try {
//						in = this.filesystem.readFile(c);
//						cert = Cert.load(in);
						cert = Cert.load(new File(c));
					} catch (CertificateEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
//					} finally {
//						if (in != null) {
//							try {
//								in.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
					} catch (CertificateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					PrivKey privKey = null;
					try {
//						in = this.filesystem.readFile(k);
						privKey = PrivKey.load(new File(k), null);

//						privKey = PrivKey.loadFromKeyStore(new File(k), null);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
//					} finally {
//						if (in != null) {
//							try {
//								in.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					} catch (UnrecoverableKeyException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (CertificateException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (KeyStoreException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidAlgorithmParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalBlockSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidParameterSpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					OPCUAKeyPairModelNode node = new OPCUAKeyPairModelNode();
					// common attributres
					node.setServerName(getServerName());
					node.setParent(this);
					node.setFilesystem(this.filesystem);
					// additional
					KeyPair keyPair = new KeyPair(cert, privKey);
					node.setKeyPair(keyPair);
					node.setCertificatePath(c);
					node.setPrivateKeyPath(k);
					nodes.add(node);

				}

			}

		}

		return nodes.toArray();
	}

	private String extractName(String name, String extractor) {
		int index = name.indexOf(extractor);
		String subname = name.substring(0, index);
		return subname;
	}

	private boolean checkFile(String file, String... extensions) {
		String ext = new Path(file).getFileExtension();

		for (String extension : extensions) {
			if (extension.equals(ext)) {
				return true;
			}
		}
		return false;
	}

}
