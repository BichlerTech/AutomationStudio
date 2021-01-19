package com.bichler.astudio.connections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.Preferences;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.xml.sax.SAXException;

import com.bichler.astudio.filesystem.HBStudioConnections;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;

public class ConnectionsHostManager {

	private HBStudioConnections studioConnections = null;

	private String path = "";

//	private String OpcUaRuntimePath;

	public ConnectionsHostManager() {

	}

	public void importHosts(String path) {
//		this.path = path;
		File file = new File(path);

		if (file.exists() && file.isFile()) {
			SAXParserFactory factory = SAXParserFactory.newInstance();

			try {
				SAXParser parser = factory.newSAXParser();
				parser.parse(file.getPath(), new ConnectionsLoader(this));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public void importHostsFromRuntimeStructure(String path, String servers) {
//		this.OpcUaRuntimePath = path;
	  Preferences _preferences = Preferences.userNodeForPackage(PickWorkspaceDialog.class);
	  String wsRoot = _preferences.get(PickWorkspaceDialog._KeyWorkspaceRootDir, "");
		if (path == null) {
			return;
		}

		IPath runtimePath = new Path(wsRoot).append(path);
		if (!runtimePath.isEmpty()) {
			// read
			ConnectionsLoader loader = new ConnectionsLoader(this);
			loader.importWorkspaceHosts(runtimePath.toOSString(), servers);
		}

	}

	public void exportHosts() {
		StringBuffer content = new StringBuffer();
		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		content.append("<ConnectionConfiguration xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" >\n");

		// export all host connections
		for (IFileSystem fs : studioConnections.getConnections().values()) {
			content.append("\t<CometConnection>\n");
			exportHost(content, fs);
			content.append("\t</CometConnection>\n");
		}

		content.append("</ConnectionConfiguration>\n");

		// open connections file to write
		File export = new File(path);
		if (!export.exists()) {
			try {
				export.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}

		if (export.exists()) {
			OutputStream out;
			try {
				out = new FileOutputStream(export);
				out.write(content.toString().getBytes());
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(content.toString());
	}

	private void exportHost(StringBuffer content, IFileSystem fs) {
		if (fs instanceof SimpleFileSystem) {
			content.append("\t\t<ConnectionType name=\"simple\" />\n");
		} else {
			content.append("\t\t<ConnectionType name=\"ssh\" />\n");
		}

		content.append("\t\t<Connectionname name=\"" + fs.getConnectionName()
				+ "\" />\n");
		content.append("\t\t<ConnectionTimeOut time=\"" + fs.getTimeOut()
				+ "\" />\n");
		content.append("\t\t<Host name=\"" + fs.getHostName() + "\" />\n");
		content.append("\t\t<User name=\"" + fs.getUser() + "\" />\n");
		content.append("\t\t<Password name=\"" + fs.getPassword() + "\" />\n");
		content.append("\t\t<JavaPath path=\"" + fs.getJavaPath() + "\" />\n");
		content.append("\t\t<JarArg arg=\"" + fs.getJavaArg() + "\" />\n");
		content.append("\t\t<RootPath name=\"" + fs.getRootPath() + "\" />\n");
		content.append("\t\t<FileSeparator name=\""
				+ fs.getTargetFileSeparator() + "\" />\n");
	}

	public HBStudioConnections getStudioConnections() {
		return studioConnections;
	}

	public void setStudioConnections(HBStudioConnections studioConnections) {
		this.studioConnections = studioConnections;
	}

}
