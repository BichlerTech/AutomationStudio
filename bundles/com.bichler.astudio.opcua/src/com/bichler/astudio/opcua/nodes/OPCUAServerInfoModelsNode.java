package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCUAServerInfoModelsNode extends OPCUAServerModelNode {
	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_OPCUAINFORMATIONMODEL);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		if (nodes == null) {
			nodes = new ArrayList<StudioModelNode>();
			String serverName = this.getServerName();
			// if (filesystem instanceof SimpleFileSystem) {
			// IPreferenceStore store = ComponentsActivator.getDefault()
			// .getPreferenceStore();
			// IPreferenceStore opcuastore = OPCUAActivator.getDefault()
			// .getPreferenceStore();
			NamespaceTable uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			String[] namespaces = uris.toArray();
			for (String namespace : namespaces) {
				OPCUAServerInfoModelNode node = new OPCUAServerInfoModelNode();
				node.setServerName(serverName);
				node.setInfModelName(namespace);
				nodes.add(node);
			}
			/** Old */
			// String path = filesystem.getRootPath()
			// + filesystem.getTargetFileSeparator()
			// + "informationmodel";
			// try {
			// if (filesystem.isDir(path)) {
			// String[] infmodels = filesystem.listFiles(path);
			// if (infmodels != null) {
			// for (String infmodel : infmodels) {
			// if (infmodel.startsWith(".")
			// || infmodel.startsWith("..")
			// || !infmodel.endsWith(".xml")
			// || infmodel.startsWith("nodeset")) {
			// continue;
			// }
			//
			// node = new OPCUAServerInfoModelNode();
			// node.setServerName(serverName);
			// node.setInfModelName(infmodel.replace(".xml",
			// ""));
			// nodes.add(node);
			// }
			// }
			//
			// }
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			this.setChildren(nodes);
		}
		return nodes.toArray();
	}
}
