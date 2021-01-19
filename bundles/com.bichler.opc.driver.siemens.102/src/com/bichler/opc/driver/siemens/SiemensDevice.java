package com.bichler.opc.driver.siemens;

import java.util.HashMap;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.AComDevice;
import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;

/**
 * This class represents the hardware connection to wire.
 * 
 * @author hannes bichler
 * @version 1.0.1
 * @company hb-softsolution
 * @contact hannes.bichler@hb-softsolution.com
 * 
 */
public class SiemensDevice extends AComDevice {
	protected ComResourceManager manager = null;
	/**
	 * list with actual to perform read requests
	 */
	// protected SiemensDPItem rdp = null;
	/**
	 * list with actual to perform write requests
	 *
	 */
	// protected ConcurrentLinkedDeque<SiemensWriteRequest> wdps = new
	// ConcurrentLinkedDeque<SiemensWriteRequest>();
	protected HashMap<Integer, SiemensDPItem> outstandigReadRequ = new HashMap<Integer, SiemensDPItem>();

	public HashMap<Integer, SiemensDPItem> getOutstandigReadRequ() {
		return outstandigReadRequ;
	}

	public void setOutstandigReadRequ(HashMap<Integer, SiemensDPItem> outstandigReadRequ) {
		this.outstandigReadRequ = outstandigReadRequ;
	}

	protected HashMap<Integer, SiemensDPItem> outstandigWriteRequ = new HashMap<Integer, SiemensDPItem>();
	private int maxByteCount = 221;

	public HashMap<Integer, SiemensDPItem> getOutstandigWriteRequ() {
		return outstandigWriteRequ;
	}

	public void setOutstandigWriteRequ(HashMap<Integer, SiemensDPItem> outstandigWriteRequ) {
		this.outstandigWriteRequ = outstandigWriteRequ;
	}

	@Override
	public boolean addDP(NodeId nodeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ComDP addDP(NodeId nodeId, Object additional) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addDPs(NodeId nodeId, Object additional) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void checkCommunication() {
		// TODO Auto-generated method stub
	}

	@Override
	public byte[] read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int write(byte[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void storeReadReq(ComDP dp) {
		// TODO Auto-generated method stub
	}

	public byte[] createReadRequest() {
		return null;
	}

	public byte[][] createWriteRequest(SiemensDPItem dp, DataValue value) throws ValueOutOfRangeException {
		return null;
	}

	public int getMaxByteCount() {
		return maxByteCount;
	}

	public void setMaxByteCount(int maxByteCount) {
		this.maxByteCount = maxByteCount;
	}

	public ComResourceManager getManager() {
		return manager;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public long connect() {
		// TODO Auto-generated method stub
		return 0;
	}
}
