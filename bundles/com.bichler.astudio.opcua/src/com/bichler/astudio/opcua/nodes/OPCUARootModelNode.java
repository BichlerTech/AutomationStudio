package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.connections.ConnectionsHostManager;
import com.bichler.astudio.connections.nodes.HostConnectionsModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;
import com.hbsoft.visu.Comet_ResourceManager;

public class OPCUARootModelNode extends HostConnectionsModelNode {
	protected Comet_ResourceManager manager = null;

	public OPCUARootModelNode() {
		super();
		Preferences _preferences = Preferences.userNodeForPackage(PickWorkspaceDialog.class);
		String wsRoot = _preferences.get(PickWorkspaceDialog._KeyWorkspaceRootDir, "");
		filesystem = new SimpleFileSystem();
		filesystem.setRootPath(wsRoot);
	}

	@Override
	public void nodeDBLClicked() {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_DATASERVER);
	}

	@Override
	public String getLabelText() {
		return CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				this.getClass().getSimpleName() + ".LabelText");
	}

	public Comet_ResourceManager getManager() {
		return manager;
	}

	public void setManager(Comet_ResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public Object[] getChildren() {
		if (this.getChildrenList() != null) {
			return this.getChildrenList().toArray();
		}
		if (connectionsManager == null) {
			connectionsManager = new ConnectionsHostManager();
			IPreferenceStore opcuastore = OPCUAActivator.getDefault().getPreferenceStore();
			String runtimeFolder = opcuastore.getString(OPCUAConstants.OPCUARuntime);
			String servers = opcuastore.getString(OPCUAConstants.ASOPCUAServersPath);
			connectionsManager.importHostsFromRuntimeStructure(runtimeFolder, servers);
		}
		List<StudioModelNode> children = new ArrayList<StudioModelNode>();
		if (connectionsManager.getStudioConnections() != null) {
			for (IFileSystem fs : connectionsManager.getStudioConnections().getConnections().values()) {
				OPCUAServerModelNode node = new OPCUAServerModelNode();
				node.setServerName(fs.getConnectionName());
				node.setFilesystem(fs);
				children.add(node);
			}
		}
		this.setChildren(children);
		return this.getChildrenList().toArray();
	}

	@Override
	public void refresh() {
		super.refresh();
		setConnectionsManager(null);
	}
}
