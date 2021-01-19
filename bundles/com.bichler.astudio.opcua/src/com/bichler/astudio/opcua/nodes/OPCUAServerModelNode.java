package com.bichler.astudio.opcua.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.images.StudioImageActivator;
import com.bichler.astudio.images.StudioImages;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.navigation.views.providers.StudioModelContentProvider;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.nodes.security.OPCUAServerSecurityModelNode;

public class OPCUAServerModelNode extends OPCUAServersModelNode {
	protected String serverName = "";
	private boolean valid = true;

	public OPCUAServerModelNode() {
		super();
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public void nodeDBLClicked() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		OPCUAUtil.openProjectPerspective(window, this);
	}

	@Override
	public Image getLabelImage() {
		return StudioImageActivator.getImage(StudioImages.ICON_SERVER);
	}

	@Override
	public String getLabelText() {
		return this.getServerName();
	}

	@Override
	public Object[] getChildren() {
		List<StudioModelNode> nodes = this.getChildrenList();
		if (nodes == null) {
			nodes = new ArrayList<>();
			String serverName = this.getServerName();

			OPCUAServerDriversModelNode drvsnode = new OPCUAServerDriversModelNode();
			drvsnode.setServerName(serverName);
			drvsnode.setFilesystem(filesystem);
			drvsnode.setParent(this);
			nodes.add(drvsnode);

			// module user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerModulesModelNode modulesnode = new OPCUAServerModulesModelNode();
				modulesnode.setServerName(serverName);
				modulesnode.setFilesystem(filesystem);
				modulesnode.setParent(this);
				nodes.add(modulesnode);
			}
			// server configuration user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerConfigModelNode confnode = new OPCUAServerConfigModelNode();
				confnode.setServerName(serverName);
				confnode.setFilesystem(filesystem);
				confnode.setParent(this);
				nodes.add(confnode);
			}

			// history user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerHistoryModelNode historynode = new OPCUAServerHistoryModelNode();
				historynode.setServerName(serverName);
				historynode.setFilesystem(filesystem);
				historynode.setParent(this);
				nodes.add(historynode);
			}

			OPCUAServerVersionsModelNode versionsnode = new OPCUAServerVersionsModelNode();
			versionsnode.setServerName(serverName);
			versionsnode.setFilesystem(filesystem);
			versionsnode.setParent(this);
			nodes.add(versionsnode);

			// information user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerInfoModelsNode infmodelsnode = new OPCUAServerInfoModelsNode();
				infmodelsnode.setServerName(serverName);
				infmodelsnode.setFilesystem(filesystem);
				infmodelsnode.setParent(this);
				nodes.add(infmodelsnode);
			}

			// security user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerSecurityModelNode certnode = new OPCUAServerSecurityModelNode();
				certnode.setServerName(serverName);
				certnode.setFilesystem(filesystem);
				certnode.setParent(this);
				nodes.add(certnode);
			}

			// report user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAReportModelNode reportnode = new OPCUAReportModelNode();
				reportnode.setServerName(serverName);
				reportnode.setFilesystem(filesystem);
				reportnode.setParent(this);
				nodes.add(reportnode);
			}

			// script user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerScriptsModelNode scripts = new OPCUAServerScriptsModelNode();
				scripts.setServerName(serverName);
				scripts.setFilesystem(filesystem);
				scripts.setParent(this);
				nodes.add(scripts);
			}

			OPCUAServerUsersModelNode users = new OPCUAServerUsersModelNode();
			users.setServerName(serverName);
			users.setFilesystem(filesystem);
			users.setParent(this);
			nodes.add(users);

			// logging user rights
			if (UserUtils.testUserRights(1)) {
				OPCUAServerLoggingModelNode logging = new OPCUAServerLoggingModelNode();
				logging.setServerName(serverName);
				logging.setFilesystem(filesystem);
				logging.setParent(this);
				nodes.add(logging);
			}

			setChildren(nodes);
		}
		return nodes.toArray();
	}

	@Override
	public boolean showChildren(IContentProvider cp) {
		if (cp instanceof StudioModelContentProvider) {
			return false;
		}
		return super.showChildren(cp);
	}

	public void setResourceValid(boolean valid) {
		this.valid = valid;
		Object[] children = getChildren();
		if (children == null) {
			return;
		}
		for (Object child : children) {
			if (child instanceof IValidationNode) {
				((IValidationNode) child).setResourceValid(valid);
			}
		}
	}

	protected boolean isResourceValid() {
		return this.valid;
	}

	/**
	 * Gets the invalid image. This case there is no invalid image.
	 * 
	 * @return
	 */
	public Image getInvalidImage() {
		return null;
	}
}
