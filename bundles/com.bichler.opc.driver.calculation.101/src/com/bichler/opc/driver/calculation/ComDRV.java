package com.bichler.opc.driver.calculation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComDRVStates;
import com.bichler.opc.comdrv.utils.ComStatusUtils;

public class ComDRV extends com.bichler.opc.comdrv.ComDRV {

	private final Logger LOGGER;
	private CalculationResourceManager manager = null;
	private ComStatusUtils utils = new ComStatusUtils();
	private CalculationUtils calcUtils = null;
	
	@Override
	public void setDriverId(long drvId) {
		super.setDriverId(drvId);
		calcUtils.setDrvName(drvName);
	}

	@Override
	public void setDriverName(String drvName) {
		super.setDriverName(drvName);
		calcUtils.setDrvName(drvName);
	}

	public ComDRV() {
		LOGGER = Logger.getLogger(getClass().getName());
		this.manager = new CalculationResourceManager();
		this.manager.setManager(drvManager.getResourceManager());
		calcUtils = new CalculationUtils();
	}

	@Override
	public boolean stop() {
		this.state = ComDRVStates.STOPPED;
		return super.stop();
	}

	@Override
	public boolean start() {
		this.state = ComDRVStates.RUNNING;
		return true;
	}

	/**
	 * get state of the Device
	 * 
	 * @return actual Device state
	 */
	public long getDeviceState() {
		return ComCommunicationStates.OPEN;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void checkCommunication() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean doStartup() {
		CalculationReadHandler readHandler = new CalculationReadHandler(calcUtils);
		readHandler.setManager(manager);
		CalculationWriteHandler writeHandler = new CalculationWriteHandler(calcUtils);
		writeHandler.setManager(manager);
		// CometDRVManager
		DataValue response = drvManager.readFromDriver(Identifiers.Server_NamespaceArray, Attributes.Value, null, null,
				(long) drvId, 0.0, TimestampsToReturn.Neither);
		/* check the namespace object */
		if (response == null || response.getValue() == null) {
			// now we return an empty datapoints list
			return true;
		}
		String[] namespaces = (String[]) response.getValue().getValue();
		NamespaceTable uris = NamespaceTable.createFromArray(namespaces);
		InputStream input;
		try {
			input = new FileInputStream("drivers/" + this.getDriverName() + "/datapoints.com");
			// now load all datapoints from xml
			CalculationImporter importer = new CalculationImporter();
			List<CalculationDP> dps = importer.loadDPs(input, uris, manager, drvId);
			// all nodes to map nodeid to datapoint
			int index = 0;
			for (CalculationDP dp : dps) {
				calcUtils.addScript("function exec" + index + "() {" + dp.getScript() + "}");
				dp.setFuncCall("exec" + index);
				index ++;
				if (!dp.isActive()) {
					continue;
				}
				switch (dp.getEvent()) {
				case CYCLIC:
					manager.addCalcInstructionCyclic(dp);
					break;
				case ONREAD:
					manager.addCalcInstructionOnRead(dp.getTarget().getTargetId(), dp);
					break;
				case VALUECHANGE:
					break;
				}
				if (dp.getEvent() == CalcEvent.ONREAD)
					// set read flag to node
					manager.getManager().getDriverConnection().setDriverReadConnected(dp.getTarget().getTargetId(),
							true, /**
									 * 
									 * this node is connected to a driver
									 */
							ComDRVManager.SYNCREAD, (long) drvId);
			}
			for (NodeId id : manager.getCalcInstructionsValueChange().keySet()) {
				// set read flag to node
				manager.getManager().getDriverConnection().setDriverWriteConnected(id, true, /**
																								 * 
																								 * this node is
																								 * connected to a driver
																								 */
						ComDRVManager.SYNCWRITE, (long) drvId);
			}
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		readHandler.setDrvId(drvId);
		drvManager.addReadListener((long) drvId, readHandler);
		writeHandler.setDrvId(drvId);
		drvManager.addWriteListener((long) drvId, writeHandler);
		return true;
	}

	@Override
	public boolean doWork() {
		if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
			// lastwatchdogwrite = System.currentTimeMillis();
			lastwatchdogwrite = System.nanoTime();
			/**
			 * now we finished run - so update watchdog state
			 */
			utils.updateWatchdog();
			// utils.updateServerWatchdog();
		}
		boolean worked = false;
		for (CalculationDP dp : this.manager.getCalcInstructionsCyclic()) {
			if (dp.getLastcalc() + dp.getTimeout() < System.nanoTime()) {
				worked = true;
				dp.setLastcalc(System.nanoTime());
				calcUtils.executeInstruction(dp);
			}
		}
		return worked;
	}

	private long lastwatchdogwrite = 0;

	@Override
	public boolean doFinish() {
		return false;
	}

	@Override
	public void setDrvReconnectTimeout(String timeout) {
		// TODO Auto-generated method stub
	}
}
