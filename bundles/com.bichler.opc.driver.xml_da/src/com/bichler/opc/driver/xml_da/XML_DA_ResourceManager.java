package com.bichler.opc.driver.xml_da;

import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;

/**
 * all resources for communication with mini pgios
 * 
 * @author applemc207da
 *
 */
public class XML_DA_ResourceManager {
	private XML_DA_Device device = null;
	/**
	 * all mini data points for digital input
	 */
	private Map<NodeId, XML_DA_DPItem> dpItems;
	private Map<String, List<XML_DA_DPItem>> dpByHandle;
	/**
	 * all scalar nodes identified by schedule time
	 */
	private Map<Long, XML_DA_ItemList<XML_DA_DPItem>> scalarnodes;
	/**
	 * all array nodes identified by schedule time
	 */
	private Map<Long, XML_DA_ItemList<XML_DA_DPItem>> arraynodes;

	/**
	 * 
	 * @return
	 */
	public Map<Long, XML_DA_ItemList<XML_DA_DPItem>> getScalarNodes() {
		return scalarnodes;
	}

	public void setScalarNodes(Map<Long, XML_DA_ItemList<XML_DA_DPItem>> scalarnodes) {
		this.scalarnodes = scalarnodes;
	}

	/**
	 * internal driver resource manager
	 */
	private ComResourceManager manager = null;
	private ComDRVManager drvManager = null;
	private Map<String, Com_TriggerDpItem> triggerList;

	/**
	 * constructor, initiate some required lock(synchronize) object
	 */
	public XML_DA_ResourceManager() {
		// readList = new ArrayList<byte[]>();
		// writeList = new ArrayList<byte[]>();
		// this.setWriteListLock(new Object());
	}

	/**
	 * get the internal resource manager
	 * 
	 * @return
	 */
	public ComResourceManager getManager() {
		return manager;
	}

	/**
	 * set the internal resource manager
	 * 
	 * @return
	 */
	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	public ComDRVManager getDrvManager() {
		return drvManager;
	}

	public void setDrvManager(ComDRVManager drvManager) {
		this.drvManager = drvManager;
	}

	public Map<NodeId, XML_DA_DPItem> getDpItems() {
		return dpItems;
	}

	public void setDpItems(Map<NodeId, XML_DA_DPItem> dpItems) {
		this.dpItems = dpItems;
	}

	public Map<String, Com_TriggerDpItem> getTriggerList() {
		return triggerList;
	}

	public void setTriggerList(Map<String, Com_TriggerDpItem> triggerList) {
		this.triggerList = triggerList;
	}

	public XML_DA_Device getDevice() {
		return device;
	}

	public void setDevice(XML_DA_Device device) {
		this.device = device;
	}

	public Map<String, List<XML_DA_DPItem>> getDpByHandle() {
		return dpByHandle;
	}

	public void setDpByHandle(Map<String, List<XML_DA_DPItem>> dpByHandle) {
		this.dpByHandle = dpByHandle;
	}

	public void addDpByHandle(Map<String, List<XML_DA_DPItem>> dpByHandle) {
		this.dpByHandle.putAll(dpByHandle);
	}

	public Map<Long, XML_DA_ItemList<XML_DA_DPItem>> getArraynodes() {
		return arraynodes;
	}

	public void setArraynodes(Map<Long, XML_DA_ItemList<XML_DA_DPItem>> arraynodes) {
		this.arraynodes = arraynodes;
	}
}
