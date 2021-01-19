package com.bichler.opc.driver.siemens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPPackages;
import com.bichler.opc.driver.siemens.dp.SiemensPackageList;

/**
 * all resources for communication with
 * 
 * @author applemc207da
 *
 */
public class SiemensResourceManager {
	private SiemensDevice device = null;
	/**
	 * all mini data points for digital input
	 */
	private Map<NodeId, SiemensDPItem> dpItems;
	private Map<Long, List<SiemensDPItem>> nodesByInterval;
	private Map<Long, SiemensPackageList<SiemensDPPackages>> packagesByInterval;
	private ConcurrentLinkedQueue<SiemensDPPackages> triggers2read = new ConcurrentLinkedQueue<SiemensDPPackages>();

	/**
	 * 
	 * @return
	 */
	public Map<Long, List<SiemensDPItem>> getNodesByInterval() {
		return nodesByInterval;
	}

	public void setNodesByInterval(Map<Long, List<SiemensDPItem>> nodesByInterval) {
		this.nodesByInterval = nodesByInterval;
	}

	/**
	 * list with all datapoints to trigger an read of list of other datapoints
	 */
	private Map<String, Com_TriggerDpItem> triggerList = new HashMap<String, Com_TriggerDpItem>();
	private Map<String, List<SiemensDPPackages>> triggerpackages;
	/**
	 * internal driver resource manager
	 */
	private ComResourceManager manager = null;
	private ConcurrentLinkedQueue<SiemensWriteRequest> writeRequests = new ConcurrentLinkedQueue<>();
	private List<NodeId> possibleTNIDs;

	/**
	 * constructor, initiate some required lock(synchronize) object
	 */
	public SiemensResourceManager() {
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

	public Map<NodeId, SiemensDPItem> getDpItems() {
		return dpItems;
	}

	public void setDpItems(Map<NodeId, SiemensDPItem> dpItems) {
		this.dpItems = dpItems;
	}

	public Map<String, Com_TriggerDpItem> getTriggerList() {
		return triggerList;
	}

	public void setTriggerList(Map<String, Com_TriggerDpItem> triggerList) {
		this.triggerList = triggerList;
	}

	public SiemensDevice getDevice() {
		return device;
	}

	public void setDevice(SiemensDevice device) {
		this.device = device;
	}

	public Map<Long, SiemensPackageList<SiemensDPPackages>> getPackagesByInterval() {
		return this.packagesByInterval;
	}

	public void setPackagesByInterval(HashMap<Long, SiemensPackageList<SiemensDPPackages>> packagesByInterval) {
		this.packagesByInterval = packagesByInterval;
	}

	public Map<String, List<SiemensDPPackages>> getTriggerpackages() {
		return triggerpackages;
	}

	public void setTriggerpackages(Map<String, List<SiemensDPPackages>> triggerpackages) {
		this.triggerpackages = triggerpackages;
	}

	public ConcurrentLinkedQueue<SiemensDPPackages> getTriggers2read() {
		return triggers2read;
	}

	public void setTriggers2read(ConcurrentLinkedQueue<SiemensDPPackages> triggers2read) {
		this.triggers2read = triggers2read;
	}

	public ConcurrentLinkedQueue<SiemensWriteRequest> getWriteRequests() {
		return writeRequests;
	}

	public void setWriteRequests(ConcurrentLinkedQueue<SiemensWriteRequest> writeRequests) {
		this.writeRequests = writeRequests;
	}

	public void setPossibleTriggerNIDs(List<NodeId> possibleTNIDs) {
		this.possibleTNIDs = possibleTNIDs;
	}

	public List<NodeId> getPossibleTriggerNIDs() {
		return this.possibleTNIDs;
	}
}
