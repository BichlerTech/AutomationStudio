package com.bichler.astudio.opcua.nodes;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.opcua.handlers.OpenOPCUAReportHandler;
import com.hbsoft.visu.Comet_ResourceManager;

public class OPCUAReportModelNode extends OPCUAServerModelNode {
	protected Comet_ResourceManager manager = null;

	public OPCUAReportModelNode() {
		super();
	}

	@Override
	public void nodeDBLClicked() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
		try {
			handlerService.executeCommand(OpenOPCUAReportHandler.ID, new Event());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_DATASERVER);
	}

	@Override
	public String getLabelText() {
		return "Reports";
	}

	public Comet_ResourceManager getManager() {
		return manager;
	}

	public void setManager(Comet_ResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public Object[] getChildren() {
		// if (this.getChildrenList() != null) {
		// return this.getChildrenList().toArray();
		// }
		// if (connectionsManager == null) {
		// connectionsManager = new ConnectionsHostManager();
		//
		// IPreferenceStore opcuastore = OPCUAActivator.getDefault()
		// .getPreferenceStore();
		// String runtimeFolder = opcuastore
		// .getString(OPCUAConstants.OPCUARuntime);
		// String servers = opcuastore
		// .getString(OPCUAConstants.CometOPCUAServersPath);
		// connectionsManager.importHostsFromRuntimeStructure(runtimeFolder,
		// servers);
		//
		// }
		//
		// List<StudioModelNode> children = new ArrayList<StudioModelNode>();
		// if (connectionsManager.getStudioConnections() != null) {
		// for (IFileSystem fs : connectionsManager
		// .getStudioConnections().getConnections().values()) {
		// OPCUAServerModelNode node = new OPCUAServerModelNode();
		// node.setServerName(fs.getConnectionName());
		// node.setFilesystem(fs);
		//
		// children.add(node);
		// }
		// }
		//
		// this.setChildren(children);
		return new Object[0]; // this.getChildrenList().toArray();
	}
	// @Override
	// public void refresh() {
	// super.refresh();
	// setConnectionsManager(null);
	// }
}
