package com.bichler.opc.driver.siemens;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.IWriteListener;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPPackages;

/**
 * 
 * @author hannes bichler
 * 
 */
public class SiemensWriteHandler implements IWriteListener {
	/**
	 * siemens resource manager with digital and pwm write queue
	 */
	private SiemensResourceManager manager = null;

	/**
	 * prepares a node for write, here we set nowrite to all called nodes because we
	 * have previously set the async write flag to each writable node.
	 */
	@Override
	public boolean prepareWrite(NodeId nodeId) {
		/** we had already initiated all write flags for nodes of that driver */
		return false;
	}

	/**
	 * 
	 */
	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		// we need to check if we have an trigger nodeid to write
		return null;
	}

	/**
	 * 
	 */
	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		return null;
	}

	/**
	 * synch write
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		return null;
	}

	/**
	 * synch write
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		if (manager.getPossibleTriggerNIDs() != null && manager.getPossibleTriggerNIDs().contains(nodeId)) {
			// has the value changed
			for (Com_TriggerDpItem item : manager.getTriggerList().values()) {
				if (nodeId.equals(item.getNodeId())) {
					// trigger found, check if it is an array definition
					if (item.getIndex() == -1) {
						if (this.manager.getTriggerpackages() != null) {
							// verify new value
							if (!value.getValue().equals(oldValue.getValue())) {
								List<SiemensDPPackages> pack = manager.getTriggerpackages().get(item.getTriggerID());
								this.manager.getTriggers2read().addAll(pack);
							}
						}
					} else {
						if (!value.getValue().isArray()) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE,
									"We should have an array as trigger, but we got a scalar! Triggernode: "
											+ item.getTriggerID());
						} else {
							Object[] objn = (Object[]) value.getValue().getValue();
							Object[] objo = (Object[]) oldValue.getValue().getValue();
							if (objn.length - 1 < item.getIndex()) {
								Logger.getLogger(getClass().getName()).log(Level.SEVERE,
										"Wrong index for new trigger value! Triggernode: " + item.getNodeId()
												+ " Index: " + (objn.length - 1));
							} else {
								if (!objn[item.getIndex()].equals(objo[item.getIndex()])) {
									if (this.manager.getTriggerpackages() != null) {
										List<SiemensDPPackages> pack = manager.getTriggerpackages()
												.get(item.getTriggerID());
										this.manager.getTriggers2read().addAll(pack);
									}
								}
							}
						}
					}
				}
			}
			// return StatusCode.GOOD;
		}
		// now we put write list to the synchron write queue
		if (manager.getDpItems().containsKey(nodeId)) {
			// at the moment we do not allow arrays to write
			// if (value.getValue().isArray()) {
			// return new StatusCode(StatusCodes.Bad_InvalidArgument);
			// }
			SiemensDPItem dp = manager.getDpItems().get(nodeId);
			SiemensTCPISODevice dev = (SiemensTCPISODevice) manager.getDevice();
			// first verify connected plc
			if (dev.getDeviceState() == ComCommunicationStates.CLOSED) {
				// write not possible because no connection to plc
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"We can not write value to plc, because plc is not connected! Node: " + dp.getNodeId()
								+ " Value: " + value.getValue());
				return new StatusCode(StatusCodes.Bad_NoCommunication);
			}
			try {
				// long startwrite = System.currentTimeMillis();
				long startwrite = System.nanoTime();
				if (((SiemensTCPISODevice) manager.getDevice()).writeMessage(dp, value)) {
					if (this.manager.getManager().isActivatedebug() && (this.manager.getManager().getDebug()
							& this.manager.getManager().DEBUG_WRITE) == this.manager.getManager().DEBUG_WRITE) {
						if (this.manager.getManager().getNids().contains(nodeId)) {
							Logger.getLogger(getClass().getName())
									.info("Write node to plc! Nodeid: " + nodeId + " value: " + value + " in " + (System
											// .currentTimeMillis() -
											// startwrite)
											.nanoTime() - startwrite) / 1000000L + " milliseconds!");
						}
					}
					return StatusCode.GOOD;
				}
				return new StatusCode(StatusCodes.Bad_InvalidState);
			} catch (ValueOutOfRangeException ex) {
				return new StatusCode(StatusCodes.Bad_OutOfRange);
			}
		}
		return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
	}

	/**
	 * get the mini resource manager
	 * 
	 * @return mini resource manager
	 */
	public SiemensResourceManager getManager() {
		return manager;
	}

	/**
	 * set the mini resource manager
	 * 
	 * @param mini resource manager
	 */
	public void setManager(SiemensResourceManager manager) {
		this.manager = manager;
	}
}
