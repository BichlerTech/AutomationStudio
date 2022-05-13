package com.bichler.opc.driver.ethernet_ip;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.IWriteListener;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;

import etherip.types.CIPData;

/**
 * 
 * @author hannes bichler
 * 
 */
public class EthernetIPWriteHandler implements IWriteListener {
	private ComEthernetIPResourceManager manager;
	private Logger logger = Logger.getLogger(getClass().getName());

	public ComEthernetIPResourceManager getManager() {
		return manager;
	}

	public void setManager(ComEthernetIPResourceManager manager) {
		this.manager = manager;
	}

	/**
	 * prepares a node for write, here we set nowrite to all called nodes because we
	 * have previously set the async write flag to each writable node.
	 */
	@Override
	public boolean prepareWrite(NodeId nodeId) {
		return false;
	}

	/**
	 * 
	 */
	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
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
						if (!value.getValue().equals(oldValue.getValue())) {
							for (NodeId id : item.getNodesToRead()) {
								EthernetIPDPItem d = this.manager.getDpItems().get(id);
								if (d != null)
									this.manager.getTrigger2Read().add(d);
							}
						}
					} else {
						if (!value.getValue().isArray()) {
							logger.log(Level.WARNING,
									"We should have an array as trigger, but we got a scalar! Triggernode: "
											+ item.getTriggerID());
						} else {
							Object[] objn = (Object[]) value.getValue().getValue();
							Object[] objo = (Object[]) oldValue.getValue().getValue();
							if (objn.length - 1 < item.getIndex()) {
								logger.log(Level.WARNING, "Wrong index for new trigger value! Triggernode: "
										+ item.getNodeId() + " Index: " + (objn.length - 1));
							} else {
								if (!objn[item.getIndex()].equals(objo[item.getIndex()])) {
									for (NodeId id : item.getNodesToRead()) {
										EthernetIPDPItem d = this.manager.getDpItems().get(id);
										if (d != null)
											this.manager.getTrigger2Read().add(d);
									}
								}
							}
						}
					}
				}
			}
			return StatusCode.GOOD;
		}
		if (this.manager.getDpItems().containsKey(nodeId)) {
			EthernetIPDPItem dp = this.manager.getDpItems().get(nodeId);
			CIPData data;
			try {
				data = dp.prog2DRV(value.getValue());
				dp.setOldDataValue(value);
				if (this.manager.getDevice().write(dp.getTagname(), data) != 0) {
					return StatusCode.BAD;
				}
			} catch (ValueOutOfRangeException e) {
				logger.log(Level.SEVERE, e.getMessage() + " | Opc-Node: '" + dp.getDisplayName() + "' | AB-Tag: '" + dp.getTagname() + "'");
				return new StatusCode(StatusCodes.Bad_OutOfRange);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return StatusCode.BAD;
			}
			return StatusCode.GOOD;
		}
		return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
	}
}
