package com.bichler.opc.driver.ethernet_ip;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerState;

import com.bichler.opc.comdrv.AComDevice;
import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;

import etherip.protocol.CIPMultiRequestProtocol;
import etherip.protocol.Connection;
import etherip.protocol.Encapsulation;
import etherip.protocol.MRChipReadProtocol;
import etherip.protocol.MRChipWriteProtocol;
import etherip.protocol.MessageRouterProtocol;
import etherip.protocol.RegisterSession;
import etherip.protocol.SendRRDataProtocol;
import etherip.protocol.UnconnectedSendProtocol;
import etherip.types.CIPData;
import etherip.types.CNPath;
import etherip.types.CNService;

public class ComEthernetIPDevice extends AComDevice {
	private Logger logger = Logger.getLogger(getClass().getName());
	private ComResourceManager manager = null;
	private ComDRVManager drvManager = ComDRVManager.getDRVManager();
	private int port = 0;
	//
	private int slot = 0;
	private Connection readconnection = null;
	private Connection writeconnection = null;
	private long drvId = 0;
	private ComStatusUtils utils = null;
	private boolean initState;

	@Override
	public boolean addDP(NodeId nodeId) {
		return false;
	}

	@Override
	public ComDP addDP(NodeId nodeId, Object additional) {
		return null;
	}

	@Override
	public boolean addDPs(NodeId nodeId, Object additional) {
		return false;
	}

	@Override
	public void checkCommunication() {
	}

	@Override
	public long connect() {
		logger.log(Level.INFO, "Connect to plc: " + this.deviceAddress);
		// set server state to connecting
		this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
				new UnsignedInteger[] { Attributes.Value }, null,
				new DataValue[] { new DataValue(new Variant(ServerState.Unknown)) }, new Long[] { (long) this.drvId });
		long readstate = connectRead();
		long writestate = connectWrite();
		if (readstate == ComCommunicationStates.CLOSED || writestate == ComCommunicationStates.CLOSED) {
			logger.log(Level.WARNING, "Could not connect to plc: " + this.deviceAddress);
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.CommunicationFault)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.ERROR);
			utils.updateWatchdog();
//      utils.updateServerWatchdog();
			return ComCommunicationStates.CLOSED;
		}
		logger.log(Level.INFO, "Connect to plc successfully! PLC: " + this.deviceAddress);
		if (!initState) {
			// only set to running if we passed the init state
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.Running)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.RUNNING);
			utils.updateWatchdog();
		} else {
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, new String[] { null },
					new DataValue[] { new DataValue(new Variant(ServerState.Suspended)) }, new Long[] { drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.STARTING);
			utils.updateWatchdog();
		}
		return ComCommunicationStates.OPEN;
	}

	public long connectRead() {
		if (readconnection == null) {
			try {
				readconnection = new Connection(this.deviceAddress, this.port, this.slot);
				final RegisterSession register = new RegisterSession();
				readconnection.execute(register);
				readconnection.setSession(register.getSession());
			} catch (Exception e) {
				return ComCommunicationStates.CLOSED;
			}
		}
		return ComCommunicationStates.OPEN;
	}

	public long connectWrite() {
		if (writeconnection == null) {
			try {
				writeconnection = new Connection(this.deviceAddress, this.port, this.slot);
				final RegisterSession register = new RegisterSession();
				writeconnection.execute(register);
				writeconnection.setSession(register.getSession());
			} catch (Exception e) {
				return ComCommunicationStates.CLOSED;
			}
		}
		return ComCommunicationStates.OPEN;
	}

	@Override
	public void disconnect() {
		return;
	}

	@Override
	public long getDeviceState() {
		if (this.readconnection != null && this.readconnection.getSession() != 0) {
			return ComCommunicationStates.OPEN;
		}
		return ComCommunicationStates.CLOSED;
	}

	public long getDrvId() {
		return drvId;
	}

	public ComDRVManager getDrvManager() {
		return drvManager;
	}

	public ComResourceManager getManager() {
		return manager;
	}

	public int getSlot() {
		return slot;
	}

	public ComStatusUtils getUtils() {
		return utils;
	}

	public boolean isInitState() {
		return initState;
	}

	@Override
	public byte[] read() {
		return null;
	}

	public DataValue read(EthernetIPDPItem tag) throws Exception {
		final MessageRouterProtocol[] readwrite = new MessageRouterProtocol[1];
		readwrite[0] = new MRChipReadProtocol(tag.getTagname(), tag.getArrayLength());
		final Encapsulation encap = new Encapsulation(ComEthernetIPCommands.SENDRRDATA, readconnection.getSession(),
				new SendRRDataProtocol(new UnconnectedSendProtocol(readconnection.getSlot(), new MessageRouterProtocol(
						CNService.CIP_MultiRequest, CNPath.MessageRouter(), new CIPMultiRequestProtocol(readwrite)))));
		try {
			readconnection.execute(encap);
			final MRChipReadProtocol reader = (MRChipReadProtocol) readwrite[0];
			// Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Status from message
			// router: {0} - {1}",
			// new String[] { Integer.toString(reader.getStatus()), reader.decodeStatus()
			// });
			if (reader.getStatus() == 0)
				return tag.drv2Prog(reader.getData());
			else
				return null;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			try {
				this.readconnection.close();
				this.readconnection = null;
				this.deviceState = ComCommunicationStates.CLOSED;
				// set led to blinking
			} catch (Exception e1) {
			}
			throw e;
		}
	}

	@Override
	public long reconnect() {
		logger.log(Level.INFO, "Reconnect to plc: " + this.deviceAddress);
		this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
				new UnsignedInteger[] { Attributes.Value }, null,
				new DataValue[] { new DataValue(new Variant(ServerState.Unknown)) }, new Long[] { (long) this.drvId });
		long readstate = this.connectRead();
		long writestate = this.connectWrite();
		if (readstate == ComCommunicationStates.CLOSED || writestate == ComCommunicationStates.CLOSED) {
			logger.log(Level.WARNING, "Could not reconnect to plc: " + this.deviceAddress);
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.CommunicationFault)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.ERROR);
			utils.updateWatchdog();
//      utils.updateServerWatchdog();
			return ComCommunicationStates.CLOSED;
		}
		logger.log(Level.INFO, "Reconnect to plc successfully! PLC: " + this.deviceAddress);
		if (!initState) {
			// only set to running if we passed the init state
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.Running)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.RUNNING);
			utils.updateWatchdog();
		} else {
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, new String[] { null },
					new DataValue[] { new DataValue(new Variant(10)) }, new Long[] { drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.STARTING);
			utils.updateWatchdog();
		}
		return ComCommunicationStates.OPEN;
	}

	@Override
	public boolean setDeviceAddress(String address) {
		if (address == null) {
			return false;
		}
		String[] content = address.split(";");
		if (content == null || content.length != 3)
			return false;
		this.deviceAddress = content[0];
		try {
			this.port = Integer.parseInt(content[1]);
			this.slot = Integer.parseInt(content[2]);
			return true;
		} catch (IllegalArgumentException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return false;
	}

	public void setDrvId(long drvId) {
		this.drvId = drvId;
	}

	public void setDrvManager(ComDRVManager drvManager) {
		this.drvManager = drvManager;
	}

	public void setInitState(boolean initState) {
		this.initState = initState;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setUtils(ComStatusUtils utils) {
		this.utils = utils;
	}

	@Override
	public void storeReadReq(ComDP dp) {
	}

	public int write(String tag, CIPData data) {
		final MessageRouterProtocol[] readwrite = new MessageRouterProtocol[1];
		readwrite[0] = new MRChipWriteProtocol(tag, data);
		final Encapsulation encap = new Encapsulation(ComEthernetIPCommands.SENDRRDATA, writeconnection.getSession(),
				new SendRRDataProtocol(new UnconnectedSendProtocol(writeconnection.getSlot(), new MessageRouterProtocol(
						CNService.CIP_MultiRequest, CNPath.MessageRouter(), new CIPMultiRequestProtocol(readwrite)))));
		try {
			writeconnection.execute(encap);
			final MRChipWriteProtocol writer = (MRChipWriteProtocol) readwrite[0];
			return writer.getStatus();
		} catch (Exception e) {
			try {
				this.writeconnection.close();
				this.writeconnection = null;
			} catch (Exception e1) {
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public int write(byte[] data) {
		return 0;
	}

	/**
	 * write to plc
	 * 
	 * @param data
	 * @param socket
	 * @return
	 */
	public int write(byte[] data, Socket socket) {
		if (socket != null) {
			try {
				if (socket.getOutputStream() != null) {
					socket.getOutputStream().write(data);
					socket.getOutputStream().flush();
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				// we had an write error
				return -1;
			}
		}
		return 0;
	}
}
