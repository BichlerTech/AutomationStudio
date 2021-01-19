package com.bichler.opc.driver.ethernet_ip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;

/**
 * Resourcemanager contains all resources which are required for ethernet IPC
 * Driver
 * 
 * @author Hannes Bichler
 *
 */
public class ComEthernetIPResourceManager {

	private Map<String, Com_TriggerDpItem> triggerList = new HashMap<String, Com_TriggerDpItem>();

	private ComEthernetIPDevice device = null;

	private Map<NodeId, EthernetIPDPItem> dpItems = null;

	private ConcurrentLinkedQueue<EthernetIPDPItem> trigger2Read = null;

	private Map<Long, List<EthernetIPDPItem>> nodesByInterval = null;

	public Map<String, Com_TriggerDpItem> getTriggerList() {
		return triggerList;
	}

	public void setTriggerList(Map<String, Com_TriggerDpItem> list) {
		triggerList = list;
	}

	private Map<NodeId, List<EthernetIPDPItem>> triggeritems;

	/**
	 * internal driver resource manager
	 */
	private ComResourceManager manager = null;

	private List<NodeId> possibleTNIDs;

	public ComEthernetIPResourceManager() {
		this.nodesByInterval = new HashMap<Long, List<EthernetIPDPItem>>();
		this.setTrigger2Read(new ConcurrentLinkedQueue<EthernetIPDPItem>());
		this.setTriggeritems(new HashMap<NodeId, List<EthernetIPDPItem>>());
	}

	public Map<Long, List<EthernetIPDPItem>> getNodesByInterval() {
		return nodesByInterval;
	}

	public void setNodesByInterval(Map<Long, List<EthernetIPDPItem>> nodesByInterval) {
		this.nodesByInterval = nodesByInterval;
	}

	public Map<NodeId, EthernetIPDPItem> getDpItems() {
		return dpItems;
	}

	public void setDpItems(Map<NodeId, EthernetIPDPItem> dpItems) {
		this.dpItems = dpItems;
	}

	public ComEthernetIPDevice getDevice() {
		return device;
	}

	public void setDevice(ComEthernetIPDevice device) {
		this.device = device;
	}

	public ComResourceManager getManager() {
		return manager;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	public ConcurrentLinkedQueue<EthernetIPDPItem> getTrigger2Read() {
		return trigger2Read;
	}

	public void setTrigger2Read(ConcurrentLinkedQueue<EthernetIPDPItem> trigger2Read) {
		this.trigger2Read = trigger2Read;
	}

	public Map<NodeId, List<EthernetIPDPItem>> getTriggeritems() {
		return triggeritems;
	}

	public void setTriggeritems(Map<NodeId, List<EthernetIPDPItem>> triggeritems) {
		this.triggeritems = triggeritems;
	}

	public void setPossibleTriggerNIDs(List<NodeId> possibleTNIDs) {
		this.possibleTNIDs = possibleTNIDs;
	}

	public List<NodeId> getPossibleTriggerNIDs() {
		return this.possibleTNIDs;
	}
}
