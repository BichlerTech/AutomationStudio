package com.bichler.opc.driver.calculation;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IWriteListener;

public class CalculationWriteHandler implements IWriteListener {
	
	private final Logger LOGGER;
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

	public CalculationWriteHandler(CalculationUtils utils) {
		LOGGER = Logger.getLogger(getClass().getName());
		this.utils = utils;
	}

	@Override
	public boolean prepareWrite(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		return null;
	}

	/**
	 * not used in this modbus because we can not send an message direct, before the
	 * server ask us.
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		List<CalculationDP> dps = manager.getCalcInstructionsValueChange().get(nodeId);
		if (dps == null) {
			LOGGER.log(Level.SEVERE, "Driver: " + utils.getDrvName() + " - No node with nodeid: "
					+ nodeId.toString() + " to calculate onwritevalue found!");
			return null;
		}

		for (CalculationDP dp : dps) {
			utils.executeInstruction(dp);
		}
		return StatusCode.GOOD;
	}

	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		return null;
	}

	/**
	 * not used in this modbus because we can not send an message direct, before the
	 * server ask us.
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		List<CalculationDP> dps = manager.getCalcInstructionsValueChange().get(nodeId);
		if (dps == null) {
			LOGGER.log(Level.SEVERE, "Driver: " + utils.getDrvName() + " - No node with nodeid: "
					+ nodeId.toString() + " to calculate onwritevalue found!");
			return null;
		}

		for (CalculationDP dp : dps) {
			utils.executeInstruction(dp);
		}
		return StatusCode.GOOD;
	}

	public CalculationResourceManager getManager() {
		return manager;
	}

	public void setManager(CalculationResourceManager manager) {
		this.manager = manager;
	}
}
