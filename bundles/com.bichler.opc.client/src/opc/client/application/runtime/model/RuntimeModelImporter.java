package opc.client.application.runtime.model;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

import opc.client.application.UAClient;
import opc.client.application.runtime.model.profile.AbstractProfileNode;
import opc.client.application.runtime.model.service.IRuntimeService;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public abstract class RuntimeModelImporter {
	private RuntimeModelDriver factory;

	public RuntimeModelImporter(RuntimeModelDriver factory) {
		this.factory = factory;
	}

	public RuntimeModelDriver getFactory() {
		return this.factory;
	}

	@SuppressWarnings("unchecked")
	public void importModel(String path) {
		// instance to open a project
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(path));
			// check for doctype
			DocType docType = doc.getDocType();
			String type = docType.getElementName();
			if (!ProjectsStoreConfiguration.UAJAVACLIENTPROJECTS.name().equals(type)) {
				throw new ConfigurationException(
						"Cannot open a projects from an invalid xml file with doctype: " + type + "!");
			}
			Element projectsRoot = doc.getRootElement();
			// servers <servers>
			Element serversNode = projectsRoot.getChild(ProjectsStoreConfiguration.SERVERS.name());
			// server <server>
			List<Element> children = serversNode.getChildren();
			for (Element element : children) {
				ServerNode sn = ServerNode.parse(element);
				this.factory.addServer(sn);
			}
			Element profilesChild = projectsRoot.getChild(ProjectsStoreConfiguration.PROFILES.name());
			if (profilesChild != null) {
				children = profilesChild.getChildren();
				for (Element element : children) {
					AbstractProfileNode pn = AbstractProfileNode.parse(element);
					this.factory.addProfile(pn);
				}
			}
			this.factory.mapProfilesWithServers();
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
	}

	public void initialize() {
		this.factory.initializeDatabaseConnections();
	}

	public void execute(UAClient client) {
		List<IRuntimeService> model = this.factory.createModel();
		this.factory.executeModel(client, model);
	}
}
