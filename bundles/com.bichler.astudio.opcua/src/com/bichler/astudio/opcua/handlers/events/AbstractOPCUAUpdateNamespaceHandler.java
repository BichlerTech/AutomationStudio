package com.bichler.astudio.opcua.handlers.events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import opc.sdk.core.node.mapper.NodeIdMapper;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.SAXException;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.components.file.ASUpdateable;
import com.bichler.astudio.components.ui.handler.AbstractCSUpdateHandler;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.components.ui.handler.update.IOPCUAUpdateable;
import com.bichler.astudio.opcua.driver.IOPCEditorRefresh;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.model.AbstractAdvanedParser;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedGroupNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;

public abstract class AbstractOPCUAUpdateNamespaceHandler extends AbstractCSUpdateHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		if (page == null) {
			return null;
		}
		OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);

		if (view == null) {
			return null;
		}

		StudioModelNode input = (StudioModelNode) view.getViewer().getInput();
		IFileSystem filesystem = input.getFilesystem();

		OPCUAUpdateNamespaceTableDriverParameter trigger = getCommandParameter(event);

		OPCUANamespaceUpdateable updateable = new OPCUANamespaceUpdateable(filesystem, trigger);
		executeUpdateOPCUADriver(updateable);

		return updateable;
	}

	/**
	 * Execute update for opc ua perspective.
	 * 
	 * @param updateable
	 */
	void executeUpdateOPCUADriver(OPCUANamespaceUpdateable updateable) {
		IFileSystem filesystem = updateable.getFilesystem();
		String root = filesystem.getRootPath();

		IOPCUAUpdateable trigger = (IOPCUAUpdateable) updateable.getTrigger();
		String drvName = trigger.getDrvName();
		String driverpath = new Path(root).append("drivers").append(drvName).toOSString();

		onUpdateDatapoints(updateable, driverpath);
		onUpdateAdvancedSettings(updateable, driverpath);
		onUpdateSettings(updateable, driverpath);
		// updateable
		onRefreshEditors(updateable);

	}

	/**
	 * Updates all datapoints for a given driver.
	 * 
	 * @param updateable
	 */
	public abstract void onUpdateDatapoints(ASUpdateable updateable, String path);

	public void onRefreshEditors(OPCUANamespaceUpdateable updateable) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window == null) {
			return;
		}

		IWorkbenchPage page = window.getActivePage();

		if (page == null) {
			return;
		}

		IEditorReference[] editorRefs = page.getEditorReferences();
		if (editorRefs == null) {
			return;
		}

		for (IEditorReference ref : editorRefs) {
			IEditorPart editor = ref.getEditor(true);

			if (editor instanceof IOPCEditorRefresh) {
				((IOPCEditorRefresh) editor).refreshDatapoints();
			}
		}
	}

	/**
	 * Updates all advanced setting nodes for a given driver.
	 * 
	 * @param updateable
	 */
	public void onUpdateAdvancedSettings(ASUpdateable updateable, String path) {
		updateAdvanced(updateable, path, "startconfignodes.com", AdvancedSectionType.StartConfig);
		updateAdvanced(updateable, path, "deviceconfig.com", AdvancedSectionType.DeviceConfig);
		updateAdvanced(updateable, path, "devicemapping.com", AdvancedSectionType.DeviceMapping);
		updateAdvanced(updateable, path, "counter.com", AdvancedSectionType.Counter);
	}

	/**
	 * Updates all setting nodes for a given driver. (e.g.: triggernodes)
	 * 
	 * @param updateable
	 */
	public void onUpdateSettings(ASUpdateable updateable, String path) {
		updateTriggerNodes(updateable, path);
	}

	protected OPCUAUpdateNamespaceTableDriverParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUAUpdateNamespaceTableDriverParameter trigger = (OPCUAUpdateNamespaceTableDriverParameter) evalCxt
				.getVariable(OPCUAUpdateNamespaceTableDriverParameter.PARAMETER_ID);
		return trigger;
	}

	private void changeIds(AbstractConfigNode node, Map<Integer, Integer> mapping) {

		if (node instanceof AdvancedRootConfigurationNode) {
			NodeId nodeId = ((AdvancedRootConfigurationNode) node).getRefNodeId();
			doChangeId(nodeId, mapping);
		}

		if (node instanceof AdvancedConfigurationNode) {
			NodeId addonId = ((AdvancedConfigurationNode) node).getAddonId();
			NodeId newAddon = doChangeId(addonId, mapping);
			((AdvancedConfigurationNode) node).setAddonId(newAddon);

			NodeId configNodeId = ((AdvancedConfigurationNode) node).getConfigNodeId();
			NodeId newConfig = doChangeId(configNodeId, mapping);
			((AdvancedConfigurationNode) node).setAddonId(newConfig);

			NodeId deviceId = ((AdvancedConfigurationNode) node).getDeviceId();
			NodeId newDevice = doChangeId(deviceId, mapping);
			((AdvancedConfigurationNode) node).setDeviceId(newDevice);

			NodeId groupId = ((AdvancedConfigurationNode) node).getDeviceId();
			NodeId newGroup = doChangeId(groupId, mapping);
			((AdvancedConfigurationNode) node).setGroupId(newGroup);

			NodeId meterId = ((AdvancedConfigurationNode) node).getMeterId();
			NodeId newMeter = doChangeId(meterId, mapping);
			((AdvancedConfigurationNode) node).setMeterId(newMeter);
		}

		AbstractConfigNode[] children = node.getChildren();
		if (children != null) {
			for (AbstractConfigNode config : children) {
				changeIds(config, mapping);
			}
		}
	}

	private NodeId doChangeId(NodeId nodeId, Map<Integer, Integer> mapping) {
		NodeId changed = NodeId.NULL;
		if (!NodeId.isNull(nodeId)) {
			changed = NodeIdMapper.mapNamespaceIndex(changed, mapping);
			if (NodeId.isNull(changed)) {
				return NodeId.NULL;
			}
		}

		return changed;
	}

	private void updateAdvanced(ASUpdateable updateable, String path, String name, AdvancedSectionType type) {
		// startconfig
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();

		InputStream input = null;
		try {
			String configPath = new Path(path).append(name).toOSString();
			if (!updateable.getFilesystem().isFile(configPath)) {
				return;
			}

			NamespaceTable originNsTable = trigger.getOriginNamespaceTable();
			input = updateable.getFilesystem().readFile(configPath);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			// start config nodes
			AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(type);

			AbstractAdvanedParser saxHandler = null;

			switch (type) {
			case Counter:
				saxHandler = new AdvancedGroupNodeParser(originNsTable, root, type);
				break;
			default:
				saxHandler = new AdvancedConfigurationNodeParser(originNsTable, root, type);
				break;
			}

			parser.parse(input, saxHandler);
			changeIds(root, trigger.getIndexMapping());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateTriggerNodes(ASUpdateable updateable, String path) {
		// startconfig
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) updateable.getTrigger();

		AdvancedDriverPersister import2export = new AdvancedDriverPersister();

		String triggerPath = new Path(path).append("triggernodes.com").toOSString();

		List<NodeToTrigger> triggernodes = import2export.importTriggerNodes(trigger.getOriginNamespaceTable(),
				updateable.getFilesystem(), triggerPath);

		for (NodeToTrigger node : triggernodes) {
			NodeId nodeId = node.nodeId;

			NodeId newId = doChangeId(nodeId, trigger.getIndexMapping());
			node.nodeId = newId;
		}

		// AdvancedDriverPersister exporter = new AdvancedDriverPersister();
		import2export.exportTriggerNodes(trigger.getNamespaceTable2change(), updateable.getFilesystem(), triggerPath,
				triggernodes);
	}
}
