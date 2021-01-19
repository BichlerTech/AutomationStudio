package com.bichler.astudio.opcua.handlers.validation;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.components.ui.handler.AbstractCSUpdateHandler;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.driver.IOPCEditorRefresh;
import com.bichler.astudio.opcua.handlers.events.AdvancedDriverPersister;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationDriverParameter;
import com.bichler.astudio.opcua.handlers.opcua.resource.OPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAAdvancedServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverConfigModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverDPsModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.opcua.widget.ConsumptionTemplate;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.opcua.widget.model.ConsumptionImportHandler;
import com.bichler.astudio.opcua.widget.model.DeviceConsumption;

public abstract class AbstractOPCUAValidationHandler extends AbstractCSUpdateHandler {
	/**
	 * Execute update for opc ua perspective.
	 * 
	 * @param updateable
	 */
	public void executeValidateOPCUADriver(OPCUAValidationDriverParameter trigger) {
		IFileSystem filesystem = trigger.getFilesystem();
		String root = filesystem.getRootPath();
		String drvName = trigger.getDrvName();
		String driverpath = new Path(root).append("drivers").append(drvName).toOSString();
		OPCUAServerModelNode node = trigger.getModelNode();
		// if(node instanceof OPCUAServerDriverModelNode){
		// System.out.println();
		// }
		if (node instanceof OPCUAServerDriverDPsModelNode) {
			onValidateDatapoints(trigger, driverpath);
		}
		if (node instanceof OPCUAAdvancedServerDriversModelNode) {
			onValidateAdvancedSettings(trigger, driverpath);
		}
		if (node instanceof OPCUAServerDriverConfigModelNode) {
			onValidateSettings(trigger, driverpath);
		}
		// refresh editor views
		// onRefreshEditors(trigger);
		// update navigation view
		onRefreshNavigation(trigger);
	}

	/**
	 * Updates all datapoints for a given driver.
	 * 
	 * @param driverpath
	 * 
	 * @param updateable
	 */
	public abstract void onValidateDatapoints(OPCUAValidationDriverParameter trigger, String driverpath);

	public void onRefreshEditors(OPCUAValidationDriverParameter trigger) {
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
	 * @param driverpath
	 * 
	 * @param updateable
	 */
	public void onValidateAdvancedSettings(OPCUAValidationDriverParameter trigger, String driverpath) {
		boolean valid = validateStartConfig(trigger, driverpath, "startconfignodes.com");
		valid = valid && validateDeviceConfig(trigger, driverpath, "deviceconfig.com");
		valid = valid && validateDeviceMapping(trigger, driverpath, "devicemapping.com");
		valid = valid && validateConsumption(trigger, driverpath, "consumptionconfiguration.com");
		trigger.getModelNode().setResourceValid(valid);
		// validateAdvanced(trigger, driverpath, "deviceconfig.com",
		// AdvancedSectionType.DeviceConfig);
		// validateAdvanced(trigger, driverpath, "devicemapping.com",
		// AdvancedSectionType.DeviceMapping);
		// validateAdvanced(trigger, driverpath, "counter.com",
		// AdvancedSectionType.Counter);
		// validateAdvanced(trigger, driverpath, "consumptionconfiguration.com",
		// AdvancedSectionType.Consumption);
	}

	/**
	 * Updates all setting nodes for a given driver. (e.g.: triggernodes)
	 * 
	 * @param driverpath
	 * 
	 * @param updateable
	 */
	public void onValidateSettings(OPCUAValidationDriverParameter trigger, String driverpath) {
		validateTriggerNodes(trigger, driverpath);
	}

	protected OPCUAServerDriverDPsModelNode getDriverDPModelNode(OPCUAValidationDriverParameter trigger) {
		OPCUAServerModelNode node = trigger.getModelNode();
		if (node.getClass() == OPCUAServerDriverDPsModelNode.class) {
			return (OPCUAServerDriverDPsModelNode) node;
		}
		// find dp node
		Object[] children = node.getChildren();
		// dp child node
		OPCUAServerDriverDPsModelNode dpNode = null;
		if (children != null) {
			for (Object child : children) {
				if (child instanceof OPCUAServerDriverDPsModelNode) {
					dpNode = ((OPCUAServerDriverDPsModelNode) child);
					break;
				}
			}
		}
		return dpNode;
	}

	protected OPCUAValidationDriverParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUAValidationDriverParameter trigger = (OPCUAValidationDriverParameter) evalCxt
				.getVariable(OPCUAValidationHandler.PARAMETER_ID);
		return trigger;
	}

	private void onRefreshNavigation(OPCUAValidationDriverParameter trigger) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}
		OPCNavigationView view = (OPCNavigationView) page.findView(OPCNavigationView.ID);
		if (view == null) {
			return;
		}
		// view.refresh(trigger.getModelNode());
		OPCUAServerModelNode modelnode = trigger.getModelNode();
		if (modelnode.getClass() == OPCUAServerDriverDPsModelNode.class
				|| modelnode.getClass() == OPCUAServerDriverConfigModelNode.class
				|| modelnode.getClass() == OPCUAAdvancedServerDriversModelNode.class) {
			view.update(modelnode);
		} else {
			Object[] children = trigger.getModelNode().getChildren();
			for (Object child : children) {
				view.update((StudioModelNode) child);
			}
		}
		// view.update(trigger.getModelNode());
	}

	/**
	 * Validate OPC UA settings for OPC UA drivers.
	 * 
	 * @param trigger
	 * @param path
	 * @param name
	 * @param type
	 */
	private boolean validateStartConfig(OPCUAValidationDriverParameter trigger, String path, String name) {
		// InputStream input = null;
		String configPath = new Path(path).append(name).toOSString();
		if (!trigger.getFilesystem().isFile(configPath)) {
			return true;
		}
		// NamespaceTable originNsTable = trigger.getOriginNamespaceTable();
		// input = trigger.getFilesystem().readFile(configPath);
		// SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// start config nodes
		AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(AdvancedSectionType.StartConfig);
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(nsTable,
				AdvancedSectionType.StartConfig);
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		root = importer.importAdvancedSettings(nsTable, trigger.getFilesystem(), configPath, saxHandler,
				AdvancedSectionType.StartConfig);
		return true;
	}

	/**
	 * Validate OPC UA settings for OPC UA drivers.
	 * 
	 * @param trigger
	 * @param path
	 * @param name
	 * @param type
	 */
	private boolean validateDeviceConfig(OPCUAValidationDriverParameter trigger, String path, String name) {
		// InputStream input = null;
		String configPath = new Path(path).append(name).toOSString();
		if (!trigger.getFilesystem().isFile(configPath)) {
			return true;
		}
		// NamespaceTable originNsTable = trigger.getOriginNamespaceTable();
		// input = trigger.getFilesystem().readFile(configPath);
		// SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// start config nodes
		AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(nsTable,
				AdvancedSectionType.DeviceConfig);
		root = importer.importAdvancedSettings(nsTable, trigger.getFilesystem(), configPath, saxHandler,
				AdvancedSectionType.DeviceConfig);
		if (!root.isActive()) {
			return true;
		}
		if (root.getRefNodeId() == null || NodeId.isNull(root.getRefNodeId())) {
			return false;
		}
		// validate all nodes
		for (AdvancedConfigurationNode n : root.getChildren()) {
			if (n.getDeviceId() == null || NodeId.isNull(n.getDeviceId()))
				return false;
		}
		return true;
	}

	/**
	 * Validate OPC UA settings for OPC UA drivers.
	 * 
	 * @param trigger
	 * @param path
	 * @param name
	 * @param type
	 */
	private boolean validateDeviceMapping(OPCUAValidationDriverParameter trigger, String path, String name) {
		// InputStream input = null;
		String configPath = new Path(path).append(name).toOSString();
		if (!trigger.getFilesystem().isFile(configPath)) {
			return true;
		}
		// NamespaceTable originNsTable = trigger.getOriginNamespaceTable();
		// input = trigger.getFilesystem().readFile(configPath);
		// SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// start config nodes
		AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(AdvancedSectionType.DeviceConfig);
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(nsTable,
				AdvancedSectionType.DeviceMapping);
		root = importer.importAdvancedSettings(nsTable, trigger.getFilesystem(), configPath, saxHandler,
				AdvancedSectionType.DeviceMapping);
		if (!root.isActive()) {
			return true;
		}
		if (root.getDatablock().isEmpty())
			return false;
		if (root.getStartAddress().isEmpty())
			return false;
		if (root.getRangeAddon().isEmpty())
			return false;
		if (root.getRangeGroup().isEmpty())
			return false;
		if (root.getMeterId().isEmpty())
			return false;
		// validate all nodes
		for (AdvancedConfigurationNode n : root.getChildren()) {
			if (n.getDeviceId() == null || NodeId.isNull(n.getDeviceId()))
				return false;
			if (n.getEnableId() == null || NodeId.isNull(n.getEnableId()))
				return false;
			if (n.getAddonId() == null || NodeId.isNull(n.getAddonId()))
				return false;
			if (n.getGroupId() == null || NodeId.isNull(n.getGroupId()))
				return false;
			if (n.getMeterId() == null || NodeId.isNull(n.getMeterId()))
				return false;
		}
		return true;
	}

	/**
	 * Validate OPC UA settings for OPC UA drivers.
	 * 
	 * @param trigger
	 * @param path
	 * @param name
	 * @param type
	 */
	private boolean validateConsumption(OPCUAValidationDriverParameter trigger, String path, String name) {
		// InputStream input = null;
		String configPath = new Path(path).append(name).toOSString();
		if (!trigger.getFilesystem().isFile(configPath)) {
			return true;
		}
		// NamespaceTable originNsTable = trigger.getOriginNamespaceTable();
		// input = trigger.getFilesystem().readFile(configPath);
		// SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// start config nodes
		AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(AdvancedSectionType.Consumption);
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		DeviceConsumption consumption = new DeviceConsumption();
		ConsumptionImportHandler handler = new ConsumptionImportHandler(consumption, nsTable);
		AdvancedDriverPersister importer = new AdvancedDriverPersister();
		importer.importDeviceConsumption(trigger.getFilesystem(), configPath, handler);
		if (!consumption.isActive()) {
			return true;
		}
		// load trigger nodes to verify it
		AdvancedDriverPersister import2export = new AdvancedDriverPersister();
		String triggerPath = new Path(path).append("triggernodes.com").toOSString();
		List<NodeToTrigger> triggernodes = import2export.importTriggerNodes(
				ServerInstance.getInstance().getServerInstance().getNamespaceUris(), trigger.getFilesystem(),
				triggerPath);
		// now we have to validate each template
		for (ConsumptionTemplate temp : consumption.getTemplates()) {
			if (temp.getDB().isEmpty())
				return false;
			if (temp.getStartAddress().isEmpty())
				return false;
			if (temp.getStructLength().isEmpty())
				return false;
			// now validate each node
			for (AbstractConfigNode node : temp.getItems()) {
				if (node instanceof AdvancedConfigurationNode) {
					if (((AdvancedConfigurationNode) node).getConfigNodeId().isNullNodeId())
						return false;
					// if (SIEMENS_DATA_TYPE.UNDEFINED.name().equals(((AdvancedConfigurationNode)
					// node).getDataType()))
					// return false;
					if (((AdvancedConfigurationNode) node).getTrigger() != null
							&& !((AdvancedConfigurationNode) node).getTrigger().triggerName.isEmpty()) {
						String triggername = ((AdvancedConfigurationNode) node).getTrigger().triggerName;
						for (NodeToTrigger trigg : triggernodes) {
							if (triggername.compareTo(trigg.triggerName) == 0) {
								if (trigg.active) {
									break;
								} else
									return false;
							}
						}
					}
				}
				node.getType();
			}
		}
		return true;
	}

	/**
	 * Validates triggernodes for OPC UA drivers.
	 * 
	 * @param trigger
	 * @param path
	 */
	private void validateTriggerNodes(OPCUAValidationDriverParameter trigger, String path) {
		// have we a driver config node?
		AdvancedDriverPersister import2export = new AdvancedDriverPersister();
		String triggerPath = new Path(path).append("triggernodes.com").toOSString();
		List<NodeToTrigger> triggernodes = import2export.importTriggerNodes(
				ServerInstance.getInstance().getServerInstance().getNamespaceUris(), trigger.getFilesystem(),
				triggerPath);
		for (NodeToTrigger trig : triggernodes) {
			if (trig.triggerName.isEmpty()) {
				trigger.getModelNode().setResourceValid(false);
				return;
			}
		}
		trigger.getModelNode().setResourceValid(true);
		// TODO: validate
	}
}
