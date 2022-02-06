package com.bichler.opc.driver.calculation;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IReadListener;

public class CalculationReadHandler implements IReadListener {
	private CalculationResourceManager manager = null;
	private ComDRVManager drvmanager = ComDRVManager.getDRVManager();
	private CalculationUtils utils;

	public long getDrvId() {
		return utils.getDrvId();
	}

	public void setDrvId(long drvId) {
		utils.setDrvId(drvId);
		utils.setDrvName(this.drvmanager.getDriver(drvId).getDriverName());
	}

	public CalculationReadHandler(CalculationUtils utils) {
		this.utils = utils;
	}

	@Override
	public boolean prepareRead(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncReadValue(NodeId nodeId, long senderState) {
		return null;
	}

	@Override
	public DataValue syncReadValue(NodeId nodeId, long senderState) {
		CalculationDP dp = manager.getCalcInstructionsOnRead().get(nodeId);
		if (dp == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Driver: " + utils.getDrvName()
					+ " - No node with Nodeid: " + nodeId.toString() + " to calculate onreadvalue found!");
			return null;
		}
		return utils.executeInstructionWithoutWrite(dp);
	}

	public CalculationResourceManager getManager() {
		return manager;
	}

	public void setManager(CalculationResourceManager manager) {
		this.manager = manager;
	}
}
