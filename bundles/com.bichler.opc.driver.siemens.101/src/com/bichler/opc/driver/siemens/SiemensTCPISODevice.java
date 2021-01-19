package com.bichler.opc.driver.siemens;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerState;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.communication.SiemensErrorCode;
import com.bichler.opc.driver.siemens.communication.SiemensISOTCPHeader;
import com.bichler.opc.driver.siemens.communication.SiemensPDU;
import com.bichler.opc.driver.siemens.communication.SiemensPDUType;
import com.bichler.opc.driver.siemens.communication.SiemensParametercode;
import com.bichler.opc.driver.siemens.communication.SiemensReadParamPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadRequestDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadResponseDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensTPDUCCHeader;
import com.bichler.opc.driver.siemens.communication.SiemensTPDUHeader;
import com.bichler.opc.driver.siemens.communication.SiemensWriteParamPart;
import com.bichler.opc.driver.siemens.communication.SiemensWriteRequestDataPart;
import com.bichler.opc.driver.siemens.communication.StartS7ConnectionConfirmParamPart;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPPackages;

/**
 * This class represents the hardware connection to wire.
 * 
 * @author hannes bichler
 * @version 1.0.1
 * @company hb-softsolution
 * @contact hannes.bichler@hb-softsolution.com
 * 
 */
public class SiemensTCPISODevice extends SiemensDevice {
	private boolean initState = true;
	/**
	 * tcp socket for iso connection to read
	 */
	private Socket readSocket = null;
	/**
	 * tcp socket for iso connection to write
	 */
	private Socket writeSocket = null;
	/**
	 * plc host name or ip
	 */
	private String host = "";
	/**
	 * port of iso tcp connection
	 */
	private int port = 0;
	/**
	 * rack number of communication processor of plc
	 */
	private int rack = 0;
	/**
	 * slot number of communication processor of plc
	 */
	private int slot = 0;
	private int readSequence = 1;
	private int writeSequence = 1;
	private long drvId = 0;
	private ComDRVManager drvManager = ComDRVManager.getDRVManager();
	private ComStatusUtils utils = null;

	public ComStatusUtils getUtils() {
		return utils;
	}

	public void setUtils(ComStatusUtils utils) {
		this.utils = utils;
	}

	/**
	 * Set the address of the Device.
	 * 
	 * @param address Address to set.
	 * @return true if it is a valid address otherwise false
	 */
	@Override
	public boolean setDeviceAddress(String address) {
		this.deviceAddress = address;
		this.host = address;
		return true;
	}

	/**
	 * default constructor, only set reconnect timeout
	 */
	public SiemensTCPISODevice() {
		this.reconnectTimeout = 1000;
	}

	@Override
	public long getDeviceState() {
		if (this.readSocket != null && this.readSocket.isConnected()) {
			return ComCommunicationStates.OPEN;
		}
		return ComCommunicationStates.CLOSED;
	}

	@Override
	public String getDeviceStateStr() {
		if (this.readSocket != null && this.readSocket.isConnected()) {
			return "OPEN";
		}
		return "CLOSED";
	}

	@Override
	public long connect() {
		Logger.getLogger(getClass().getName()).info("Connect to plc: " + host);
		// set server state to connecting
		this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
				new UnsignedInteger[] { Attributes.Value }, null,
				new DataValue[] { new DataValue(new Variant(ServerState.Unknown)) }, new Long[] { (long) this.drvId });
		long readstate = this.connectRead();
		long writestate = this.connectWrite();
		if (readstate == ComCommunicationStates.CLOSED || writestate == ComCommunicationStates.CLOSED) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not connect to plc: " + host);
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.CommunicationFault)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.ERROR);
//      utils.updateWatchdog();
//      utils.updateServerWatchdog();
			return ComCommunicationStates.CLOSED;
		}
		Logger.getLogger(getClass().getName()).info("Connect to plc successfully! PLC: " + host);
		if (!initState) {
			// only set to running if we passed the init state
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.Running)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.RUNNING);
//      utils.updateWatchdog();
		} else {
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, new String[] { null },
					new DataValue[] { new DataValue(new Variant(ServerState.Suspended)) }, new Long[] { drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.STARTING);
//      utils.updateWatchdog();
		}
		return ComCommunicationStates.OPEN;
	}

	/**
	 * 
	 * @return
	 */
	private long connectRead() {
		this.readSocket = new Socket();
		try {
			this.readSocket.setReceiveBufferSize(256);
			this.readSocket.setSoTimeout(100);
			readSocket.connect(new InetSocketAddress(host, port), (int) this.reconnectTimeout);
			/**
			 * now try to get connection response, and evaluate it
			 */
			int res = this.initReadCommunication();
			if (res != 0) {
				this.readSocket.close();
				this.readSocket = null;
				return ComCommunicationStates.CLOSED;
			}
			return ComCommunicationStates.OPEN;
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return ComCommunicationStates.CLOSED;
	}

	private long connectWrite() {
		this.writeSocket = new Socket();
		try {
			this.writeSocket.setReceiveBufferSize(256);
			this.writeSocket.setSoTimeout(100);
			writeSocket.connect(new InetSocketAddress(host, port), (int) this.reconnectTimeout);
			/**
			 * now try to get connection response, and evaluate it
			 */
			int res = this.initWriteCommunication();
			if (res != 0) {
				this.writeSocket.close();
				this.writeSocket = null;
				return ComCommunicationStates.CLOSED;
			}
			return ComCommunicationStates.OPEN;
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return ComCommunicationStates.CLOSED;
	}

	@Override
	public long reconnect() {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Reconnect to plc: {0}", new String[] { host });
		this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
				new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { new DataValue(new Variant(13)) },
				new Long[] { (long) this.drvId });
		long readstate = this.connectRead();
		long writestate = this.connectWrite();
		if (readstate == ComCommunicationStates.CLOSED || writestate == ComCommunicationStates.CLOSED) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not reconnect to plc: {0}",
					new String[] { host });
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(14)) }, new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.ERROR);
//      utils.updateWatchdog();
//      utils.updateServerWatchdog();
			return ComCommunicationStates.CLOSED;
		}
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Reconnect to plc successfully! PLC: {0}",
				new String[] { host });
		if (!initState) {
			// only set to running if we passed the init state
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, null,
					new DataValue[] { new DataValue(new Variant(ServerState.Running)) },
					new Long[] { (long) this.drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.RUNNING);
//      utils.updateWatchdog();
		} else {
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, new String[] { null },
					new DataValue[] { new DataValue(new Variant(10)) }, new Long[] { drvId });
			// add connection error state to server.state
			utils.writeLEDStatus(ComStatusUtils.STARTING);
//      utils.updateWatchdog();
		}
		return ComCommunicationStates.OPEN;
	}

	/**
	 * connection request to plc
	 * 
	 * @return
	 */
	private int initReadCommunication() {
		int res = 0;
		byte[] b4 = { (byte) 0x11, // length (in byte) of TPDU header
				(byte) 0xE0, // Conneciton Request code (1110) & credit (always
				// 0000)
				(byte) 0x00, // destination reference
				(byte) 0x00, // destination reference
				(byte) 0x00, // source reference
				(byte) 0x01, // source reference
				(byte) 0x00, // class option (always class 0)
				(byte) 0xC1, // 0xC1 (code: calling TSAP-ID)
				(byte) 0x02, // 0x02 (number of bytes following)
				(byte) 0x01, // 0x02 (unknown function; part of TSAP-ID)
				(byte) 0x00, // TSAP-ID (rack & slot)
				(byte) 0xC2, // 0xC2 (code: called TSAP-ID)
				(byte) 0x02, // 0x02 (number of bytes following)
				(byte) 0x01, // 0x02 (unknown function; part of TSAP-ID)
				(byte) 0x02, // TSAP-ID (rack & slot)
				(byte) 0xC0, // 0xC0 (code: TPDU size)
				(byte) 0x01, // 0x01 (number of bytes following)
				(byte) 0x0A }; // TPDU size (as exponent to base of 2)
		ComByteMessage inBuffer = new ComByteMessage();
		ComByteMessage outBuffer = new ComByteMessage();
		/**
		 * first add header to outbuffer
		 */
		SiemensISOTCPHeader header = new SiemensISOTCPHeader();
		header.setMessagesize(b4.length);
		outBuffer.addBuffer(header.toBytes());
		/**
		 * append connect message to buffer, after message header
		 */
		outBuffer.addBuffer(b4);
		outBuffer.getBuffer()[17] = (byte) (rack + 1);
		// outBuffer.getBuffer()[17] = (byte) (rack);
		outBuffer.getBuffer()[18] = (byte) slot;
		/**
		 * now send connection request to plc
		 */
		if (this.write(outBuffer.getBuffer(), this.readSocket) == -1) {
			return -1;
		}
		/**
		 * receive plc response for read socket
		 */
		boolean readymessage = false;
		long timeout = 3000L * 1000000L;
		// long startconnect = System.currentTimeMillis();
		long startconnect = System.nanoTime();
		while (!readymessage) {
			/**
			 * we have read timeout
			 */
			// if (startconnect + timeout < System.currentTimeMillis()) {
			if (startconnect + timeout < System.nanoTime()) {
				res = -1;
				break;
			}
			// read on interface
			byte[] in = this.read(this.readSocket);
			// we couldn't read any message, because we had ...
			if (in == null) {
				res = -1;
				break;
			}
			// we got an message from plc
			if (in.length > 0) {
				inBuffer.addBuffer(in);
			}
			/**
			 * is message complete?
			 */
			SiemensTPDUHeader tpdu = this.createTPDU(inBuffer);
			if (tpdu != null) {
				// creation was ok -> so do additional work
				// check protocol version
				// check ..
				if (tpdu.getHeader().getVersion() != 0x03) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"We got wrong protocol version number: " + tpdu.getHeader().getVersion());
				} else {
					readymessage = true;
					res = 0;
					continue;
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				readymessage = true;
				res = -1;
				e.printStackTrace();
			}
		}
		if (res == 0) {
			readymessage = false;
			this.readSequence = 1;
			// now start S7connection
			ComByteMessage message = this.createStartMessage(this.readSequence);
			this.readSequence++;
			this.write(message.getBuffer(), this.readSocket);
			// startconnect = System.currentTimeMillis();
			startconnect = System.nanoTime();
			while (!readymessage) {
				/**
				 * we have read timeout
				 */
				// if (startconnect + timeout < System.currentTimeMillis()) {
				if (startconnect + timeout < System.nanoTime()) {
					res = -1;
					break;
				}
				// read on interface
				byte[] in = this.read(this.readSocket);
				// we couldn't read any message, because we had ...
				if (in == null) {
					res = -1;
					break;
				}
				// we got an message from plc
				if (in.length > 0) {
					inBuffer.addBuffer(in);
				}
				SiemensTPDUHeader tpdu = this.createTPDU(inBuffer);
				// we should get StartS7ConnectionConfirmParamPart
				// now we are online
				if (tpdu != null && tpdu.getPdu() != null && tpdu.getPdu().getParam() != null
						&& tpdu.getPdu().getParam() instanceof StartS7ConnectionConfirmParamPart) {
					readymessage = true;
					this.deviceState = ComCommunicationStates.OPEN;
					res = 0;
					continue;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					readymessage = true;
					res = -1;
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	/**
	 * connection request to plc
	 * 
	 * @return
	 */
	private int initWriteCommunication() {
		int res = 0;
		byte[] b4 = { (byte) 0x11, // length (in byte) of TPDU header
				(byte) 0xE0, // Conneciton Request code (1110) & credit (always
				// 0000)
				(byte) 0x00, // destination reference
				(byte) 0x00, // destination reference
				(byte) 0x00, // source reference
				(byte) 0x01, // source reference
				(byte) 0x00, // class option (always class 0)
				(byte) 0xC1, // 0xC1 (code: calling TSAP-ID)
				(byte) 0x02, // 0x02 (number of bytes following)
				(byte) 0x01, // 0x02 (unknown function; part of TSAP-ID)
				(byte) 0x00, // TSAP-ID (rack & slot)
				(byte) 0xC2, // 0xC2 (code: called TSAP-ID)
				(byte) 0x02, // 0x02 (number of bytes following)
				(byte) 0x01, // 0x02 (unknown function; part of TSAP-ID)
				(byte) 0x02, // TSAP-ID (rack & slot)
				(byte) 0xC0, // 0xC0 (code: TPDU size)
				(byte) 0x01, // 0x01 (number of bytes following)
				(byte) 0x0A }; // TPDU size (as exponent to base of 2)
		ComByteMessage inBuffer = new ComByteMessage();
		ComByteMessage outBuffer = new ComByteMessage();
		/**
		 * first add header to outbuffer
		 */
		SiemensISOTCPHeader header = new SiemensISOTCPHeader();
		header.setMessagesize(b4.length);
		outBuffer.addBuffer(header.toBytes());
		/**
		 * append connect message to buffer, after message header
		 */
		outBuffer.addBuffer(b4);
		outBuffer.getBuffer()[17] = (byte) (rack + 1);
		outBuffer.getBuffer()[18] = (byte) slot;
		/**
		 * receive plc response for read socket
		 */
		boolean readymessage = false;
		long timeout = 3000L * 1000000L;
		// long startconnect = System.currentTimeMillis();
		long startconnect = System.nanoTime();
		// now try to connect to write socket
		if (this.write(outBuffer.getBuffer(), this.writeSocket) == -1) {
			return -1;
		}
		/**
		 * receive plc response for write socket
		 */
		readymessage = false;
		// startconnect = System.currentTimeMillis();
		startconnect = System.nanoTime();
		while (!readymessage) {
			/**
			 * we have read timeout
			 */
			// if (startconnect + timeout < System.currentTimeMillis()) {
			if (startconnect + timeout < System.nanoTime()) {
				res = -1;
				break;
			}
			// read on interface
			byte[] in = this.read(this.writeSocket);
			// we couldn't read any message, because we had ...
			if (in == null) {
				res = -1;
				break;
			}
			// we got an message from plc
			if (in.length > 0) {
				inBuffer.addBuffer(in);
			}
			/**
			 * is message complete?
			 */
			SiemensTPDUHeader tpdu = this.createTPDU(inBuffer);
			if (tpdu != null) {
				// creation was ok -> so do additional work
				// check protocol version
				// check ..
				if (tpdu.getHeader().getVersion() != 0x03) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"We got wrong protocol version number: " + tpdu.getHeader().getVersion());
				} else {
					readymessage = true;
					res = 0;
					continue;
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				readymessage = true;
				res = -1;
				e.printStackTrace();
			}
		}
		if (res == 0) {
			readymessage = false;
			this.writeSequence = 1;
			// now start S7connection
			ComByteMessage message = this.createStartMessage(this.writeSequence);
			this.writeSequence++;
			this.write(message.getBuffer(), this.writeSocket);
			// startconnect = System.currentTimeMillis();
			startconnect = System.nanoTime();
			while (!readymessage) {
				/**
				 * we have read timeout
				 */
				// if (startconnect + timeout < System.currentTimeMillis()) {
				if (startconnect + timeout < System.nanoTime()) {
					res = -1;
					break;
				}
				// read on interface
				byte[] in = this.read(this.writeSocket);
				// we couldn't read any message, because we had ...
				if (in == null) {
					res = -1;
					break;
				}
				// we got an message from plc
				if (in.length > 0) {
					inBuffer.addBuffer(in);
				}
				SiemensTPDUHeader tpdu = this.createTPDU(inBuffer);
				// we should get StartS7ConnectionConfirmParamPart
				// now we are online
				if (tpdu != null && tpdu.getPdu() != null && tpdu.getPdu().getParam() != null
						&& tpdu.getPdu().getParam() instanceof StartS7ConnectionConfirmParamPart) {
					readymessage = true;
					this.deviceState = ComCommunicationStates.OPEN;
					res = 0;
					continue;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					readymessage = true;
					res = -1;
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	/**
	 * creates a complete s7 connection start message
	 * 
	 * @return
	 */
	private ComByteMessage createStartMessage(int sequence) {
		ComByteMessage message = new ComByteMessage();
		this.addISOTCPHeader(message, (byte) 0x19);
		this.addTPDUHeader(message);
		this.addStartConnectionPDU(message, sequence);
		this.addStartConnectionParameter(message);
		return message;
	}

	/**
	 * adds a default iso tcp header inclusive message length into the message
	 * buffer
	 * 
	 * @param message
	 * @param size
	 */
	private void addISOTCPHeader(ComByteMessage message, SiemensDPItem dp) {
		int islength = 19;
		// Iterator<SiemensWriteRequest> iterator = wdps.iterator();
		if (dp != null) {
			islength += 12 + 4 + dp.getLength() * dp.getWriteArraylength(-1);
			// islength += 12 + 4 + dp.getLength() *
			// dp.getWriteArraylengthinByte(-1);
		}
		this.addISOTCPHeader(message, islength);
	}

	/**
	 * adds a default iso tcp header inclusive message length into the message
	 * buffer
	 * 
	 * @param message
	 * @param size
	 */
	public void addISOTCPHeader(ComByteMessage message, int size) {
		byte[] data = new byte[] { (byte) 0x03, 0, 0, (byte) size };
		message.addBuffer(data);
	}

	/**
	 * adds a default tpdu header into the message buffer
	 * 
	 * @param message
	 */
	public void addTPDUHeader(ComByteMessage message) {
		byte[] data = new byte[] { 0x02, (byte) 0xF0, (byte) 0x80 };
		message.addBuffer(data);
	}

	/**
	 * add start connection pdu
	 * 
	 * @param message
	 */
	private void addStartConnectionPDU(ComByteMessage message, int sequence) {
		byte[] data = new byte[] { 0x32, SiemensPDUType.REQUEST.getType(), 0, 0, (byte) (sequence / 256),
				(byte) (sequence % 256), 0, 0x08, 0, 0 };
		message.addBuffer(data);
	}

	public void addReadPDU(ComByteMessage message, int paramsize) {
		byte[] data = new byte[] { 0x32, SiemensPDUType.REQUEST.getType(), 0x00, 0x00, (byte) (this.readSequence / 256),
				(byte) (this.readSequence % 256), (byte) (paramsize / 256), (byte) (paramsize % 256), 0x00, 0x00 };
		message.addBuffer(data);
	}

	/**
	 * add the pdu to write any data
	 * 
	 * @param message
	 * @param paramsize
	 */
	public void addWritePDU(ComByteMessage message, int paramsize, int datalength) {
		byte[] data = new byte[] { 0x32, SiemensPDUType.REQUEST.getType(), 0x00, 0x00,
				(byte) (this.writeSequence / 256), (byte) (this.writeSequence % 256), (byte) (paramsize / 256),
				(byte) (paramsize % 256), (byte) (datalength / 256), (byte) (datalength % 256) };
		message.addBuffer(data);
	}

	private void addStartConnectionParameter(ComByteMessage message) {
		byte[] data = new byte[] { SiemensParametercode.OPEN_S7_CONNECTION.getType(), 0, 0, 1, 0, 3, 0, (byte) 0xF0 };
		message.addBuffer(data);
	}

	public void addWriteParameter(ComByteMessage message /*
															 * , SiemensDPItem dp
															 */) {
		byte[] data = new byte[] { SiemensParametercode.WRITE.getType(), (byte) 1 };
		message.addBuffer(data);
	}

	public void addReadParameter(ComByteMessage message, byte datacount) {
		byte[] data = new byte[] { SiemensParametercode.READ.getType(), datacount };
		message.addBuffer(data);
	}

	public void addWriteData(ComByteMessage message, SiemensDPItem dp, DataValue val, int i)
			throws ValueOutOfRangeException {
		byte[] data = null;
		if (dp != null && val != null) {
			if (i < 0) {
				byte[] bval = dp.prog2DRV(val);
				data = new byte[12 + 4 + bval.length];
				// add write request part
				data[0] = 0x12; // unknown
				data[1] = 0x0A; // 10 byte following data
				data[2] = 0x10; // unknown
				data[3] = (byte) dp.getDataType().getType();
				// int length = dp.getWriteArraylengthinByte(i);
				int length = dp.getWriteArraylength(i);
				data[4] = (byte) (length / 256);
				data[5] = (byte) (length % 256); // 0x01;
				data[6] = (byte) (dp.getDBAddress() / 256);
				data[7] = (byte) (dp.getDBAddress() % 256);
				data[8] = (byte) dp.getAddressType().getCode();
				data[9] = (byte) (dp.getWriteIndexInBit(-1) / 256 / 256);
				data[10] = (byte) ((dp.getWriteIndexInBit(-1) / 256) % 256);
				data[11] = (byte) (dp.getWriteIndexInBit(-1) % 256);
				// add data part
				data[12] = 0; // Fehlercode
				data[13] = (byte) dp.getDataKind().getKind();
				data[14] = (byte) (dp.getLengthInBit() * dp.getWriteArraylength(i) / 256);
				data[15] = (byte) (dp.getLengthInBit() * dp.getWriteArraylength(i) % 256);
				// now create value to write
				for (int j = 0; j < bval.length; j++) {
					data[16 + j] = bval[j];
				}
				message.addBuffer(data);
			} else {
				byte[] bval = dp.prog2DRV(val);
				data = new byte[12 + 4 + dp.getWriteArraylength(i) * dp.getLength()];
				// add write request part
				data[0] = 0x12; // unknown
				data[1] = 0x0A; // 10 byte following data
				data[2] = 0x10; // unknown
				data[3] = (byte) dp.getDataType().getType();
				int length = dp.getWriteArraylength(i);
				data[4] = (byte) (length / 256);
				data[5] = (byte) (length % 256); // 0x01;
				// data[4] = 0x00;
				// data[5] = 0x01;
				data[6] = (byte) (dp.getDBAddress() / 256);
				data[7] = (byte) (dp.getDBAddress() % 256);
				data[8] = (byte) dp.getAddressType().getCode();
				int index = dp.getWriteIndexInBit(i);
				data[9] = (byte) (index / 256 / 256);
				data[10] = (byte) ((index / 256) % 256);
				data[11] = (byte) (index % 256);
				// add data part
				data[12] = 0; // Fehlercode
				data[13] = (byte) dp.getDataKind().getKind();
				data[14] = (byte) (dp.getLengthInBit() * dp.getWriteArraylength(i) / 256);
				data[15] = (byte) (dp.getLengthInBit() * dp.getWriteArraylength(i) % 256);
				// now create value to write
				length = dp.getWriteArraylength(i - 1);
				for (int j = 0; j < data.length - 16; j++) {
					data[16 + j] = bval[(dp.getLength() * length * i) + j];
				}
				message.addBuffer(data);
			}
		}
	}

	/**
	 * 
	 * @param message
	 * @param dps
	 * @param index
	 */
	public void addReadData(ComByteMessage message, List<SiemensDPItem> dps, int index) {
		byte[] data = new byte[12];
		for (int i = 0; i < dps.size(); i++) {
			SiemensDPItem dp = dps.get(i);
			// int size = dps.size();
			data[0] = 0x12; // unknown
			data[1] = 0x0A; // 10 byte following data
			data[2] = 0x10; // unknown
			data[3] = (byte) dp.getDataType().getType();
			int length = dp.getReadArraylength(index);
			data[4] = (byte) (length / 256);
			data[5] = (byte) (length % 256); // 0x01;
			data[6] = (byte) (dp.getDBAddress() / 256);
			data[7] = (byte) (dp.getDBAddress() % 256);
			data[8] = (byte) dp.getAddressType().getCode();
			int indexinbit = dp.getReadIndexInBit(index);
			data[9] = (byte) (indexinbit / 256 / 256);
			data[10] = (byte) ((indexinbit / 256) % 256);
			data[11] = (byte) (indexinbit % 256);
			message.addBuffer(data);
		}
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public SiemensTPDUHeader createTPDU(ComByteMessage message) {
		SiemensTPDUHeader tpdu = null;
		byte[] data = message.getBuffer();
		// first try to create iso tcp header
		SiemensISOTCPHeader isoheader = this.createISOTCPHeader(data);
		// header could be created?
		if (isoheader != null) {
			if (data.length >= isoheader.getMessagesize()) {
				// tpdu length
				byte tpdulength = data[4];
				// correct length
				byte code = data[5];
				// have we a confirm message
				switch (code) {
				case (byte) 0xE0:
					// get connection request
					// do nothing we are not the server
					break;
				case (byte) 0xD0:
					// get connection response
					tpdu = this.createCC(data);
					tpdu.setHeader(isoheader);
					// tpdu.setPdu(pdu);
					// remove all bytes
					message.deleteFirstBytes(isoheader.getMessagesize());
					// check cc response
					break;
				case (byte) 0xF0:
					// response for several requests
					tpdu = new SiemensTPDUHeader();
					tpdu.setHeader(isoheader);
					tpdu.setLength(tpdulength);
					tpdu.setNumber(data[6]);
					// now try to create pdu part
					SiemensPDU pdu = this.createPDU(data);
					tpdu.setPdu(pdu);
					// now clear read message
					message.deleteFirstBytes(isoheader.getMessagesize());
					break;
				}
			}
		}
		// correct
		return tpdu;
	}

	/**
	 * create pdu parameter part
	 * 
	 * @param data
	 * @param pdu
	 */
	private void createPDUParameter(byte[] data, SiemensPDU pdu) {
		if (data.length < 20) {
			return;
		}
		byte paramcode = data[19];
		SiemensParametercode code = SiemensParametercode.fromType(paramcode);
		switch (code) {
		case OPEN_S7_CONNECTION:
			StartS7ConnectionConfirmParamPart cc = new StartS7ConnectionConfirmParamPart();
			cc.setCode(code);
			cc.setUnknown1(data[20]);
			cc.setUnknown2(data[21]);
			cc.setUnknown3(data[22]);
			cc.setUnknown4(data[23]);
			cc.setUnknown5(data[24]);
			cc.setOfferedPDUSize(data[25]);
			pdu.setParam(cc);
			break;
		case READ:
			SiemensReadParamPart read = new SiemensReadParamPart();
			pdu.setParam(read);
			read.setCode(code);
			read.setDataCount(data[20]);
			if (pdu.getType() == SiemensPDUType.REQUEST) {
				for (int i = 0; i < read.getDataCount(); i++) {
					SiemensReadRequestDataPart d = new SiemensReadRequestDataPart();
					d.setUnknown1(data[i * 12 + 21]); // unknown byte
					d.setFollowing(data[i * 12 + 22]); // following bytecount
					d.setUnknown2(data[i * 12 + 23]); // unknown byte
					d.setDatatype(data[i * 12 + 24]); // data type
					d.setLength(new byte[] { data[i * 12 + 25], data[i * 12 + 26] }); // length
					d.setDpNumber(new byte[] { data[i * 12 + 27], data[i * 12 + 28] }); // dbnumer
					d.setAreacode(data[i * 12 + 29]); // area code
					d.setStartAddress(new byte[] { data[i * 12 + 30], data[i * 12 + 31], data[i * 12 + 32] }); // startaddress
					// in
					// bits
					read.getDataParts().add(d);
				}
			} else if (pdu.getType() == SiemensPDUType.RESPONSE) {
				int offset = 21;
				for (int i = 0; i < read.getDataCount(); i++) {
					SiemensReadResponseDataPart d = new SiemensReadResponseDataPart();
					d.setDataErrorCode(data[offset]); // data error code FF= OK
					d.setDataKind(data[offset + 1]); // data kind
					d.setLength(new byte[] { data[offset + 2], data[offset + 3] }); // length
					// of
					// following
					// bytes
					// or
					// bits
					// depending
					// on
					// data
					// type
					// also try to
					byte[] dat = new byte[d.getLengthinByte()];
					int evenlength = d.getLengthinByte();
					if (offset + d.getLengthinByte() < data.length) {
						// generate evenlength if we have following data
						if (evenlength % 2 == 1) {
							evenlength++;
						}
					}
					// copy value data to message
					System.arraycopy(data, offset + 4, dat, 0, d.getLengthinByte());
					offset += 4 + evenlength;
					d.setData(dat);
					read.getDataParts().add(d);
				}
			}
			pdu.setParam(read);
			break;
		case WRITE:
			// will be implemented later
			SiemensWriteParamPart write = new SiemensWriteParamPart();
			pdu.setParam(write);
			write.setCode(code);
			write.setDataCount(data[20]);
			if (pdu.getType() == SiemensPDUType.REQUEST) {
				for (int i = 0; i < write.getDataCount(); i++) {
					SiemensWriteRequestDataPart d = new SiemensWriteRequestDataPart();
					d.setUnknown1(data[i * 12 + 21]); // unknown byte
					d.setFollowing(data[i * 12 + 22]); // following bytecount
					d.setUnknown2(data[i * 12 + 23]); // unknown byte
					d.setDatatype(data[i * 12 + 24]); // data type
					d.setLength(new byte[] { data[i * 12 + 25], data[i * 12 + 26] }); // length
					d.setDpNumber(new byte[] { data[i * 12 + 27], data[i * 12 + 28] }); // dbnumer
					d.setAreacode(data[i * 12 + 29]); // area code
					d.setStartAddress(new byte[] { data[i * 12 + 30], data[i * 12 + 31], data[i * 12 + 32] }); // startaddress
					// in
					// bits
					write.getDataParts().add(d);
				}
			} else {
				// SiemensWriteResponseDataPart d = new
				// SiemensWriteResponseDataPart();
			}
			pdu.setParam(write);
			break;
		case UPLOAD:
			break;
		case DOWNLOAD_BLOCK:
			break;
		case END_DOWNLOAD:
			break;
		case END_UPLOAD:
			break;
		case INSERT_BLOCK:
			break;
		case REQUEST_DOWNLOAD:
			break;
		case SSL_DIAGNOSTICS:
			break;
		case START_UPLOAD:
			break;
		default:
			break;
		}
	}

	/**
	 * TODO implement it ready create connection confirm message from byte stream
	 */
	private SiemensTPDUCCHeader createCC(byte[] data) {
		SiemensTPDUCCHeader cc = new SiemensTPDUCCHeader();
		// here we need no length check
		return cc;
	}

	private SiemensPDU createPDU(byte[] data) {
		SiemensPDU pdu = new SiemensPDU();
		pdu.setStart(data[7]);
		pdu.setType(data[8]);
		pdu.setReserved(new byte[] { data[9], data[10] });
		pdu.setSequence(new byte[] { data[11], data[12] });
		pdu.setParameterlength(new byte[] { data[13], data[14] });
		pdu.setDatalength(new byte[] { data[15], data[16] });
		pdu.setErrorcode(new byte[] { data[17], data[18] });
		// validate error code
		if (pdu.getErrorcode() == SiemensErrorCode.NO_ERROR.getCode()) {
			// we have no error so do additional validation
			this.createPDUParameter(data, pdu);
		}
		return pdu;
	}

	/**
	 * create iso tcp header,
	 * 
	 * @param data
	 * @return
	 */
	public SiemensISOTCPHeader createISOTCPHeader(byte[] data) {
		SiemensISOTCPHeader header = null;
		if (data.length >= 4) {
			// first check version number
			header = new SiemensISOTCPHeader();
			header.setVersion(data[0]);
			header.setMessagesize(new byte[] { data[2], data[3] });
		}
		return header;
	}

	public Socket getReadSocket() {
		return readSocket;
	}

	public void setReadSocket(Socket socket) {
		this.readSocket = socket;
	}

	public Socket getWriteSocket() {
		return writeSocket;
	}

	public void setWriteSocket(Socket socket) {
		this.writeSocket = socket;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public boolean addDP(NodeId nodeId) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public ComDP addDP(NodeId nodeId, Object additional) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean addDPs(NodeId nodeId, Object additional) {
		// Auto-generated method stub
		return false;
	}

	@Override
	public void checkCommunication() {
		// Auto-generated method stub
	}

	/**
	 * create a write request form one datapoint mapping
	 */
	@Override
	public byte[][] createWriteRequest(SiemensDPItem dp, DataValue value) throws ValueOutOfRangeException {
		byte[][] ret;
		ComByteMessage message = new ComByteMessage();
		// write single message for a datapoint
		if (dp.getMaxWriteByteCount() > dp.getWriteArraylength(-1) * dp.getLength()) {
			this.addISOTCPHeader(message, dp);
			this.addTPDUHeader(message);
			this.addWritePDU(message, 2 + 12, 4 + dp.getLength() * dp.getWriteArraylength(-1));
			this.addWriteParameter(message/* , dp */);
			this.addWriteData(message, dp, value, -1);
			this.writeSequence++;
			if (this.writeSequence > 256 * 256) {
				this.writeSequence = 1;
			}
			ret = new byte[][] { message.getBuffer() };
		} else {
			int messagecount = dp.getReadMessageCount();
			ret = new byte[messagecount][];
			for (int i = 0; i < messagecount; i++) {
				// create more write messages to write more than 220bytes
				message = new ComByteMessage();
				// header length =
				int islength = 19;
				if (dp != null) {
					islength += 12 + 4 + dp.getLength() * dp.getWriteArraylength(i);
				}
				this.addISOTCPHeader(message, islength);
				this.addTPDUHeader(message);
				this.addWritePDU(message, 2 + 12, 4 + dp.getLength() * dp.getWriteArraylength(i));
				this.addWriteParameter(message);
				this.addWriteData(message, dp, value, i);
				this.writeSequence++;
				if (this.writeSequence > 256 * 256) {
					this.writeSequence = 1;
				}
				ret[i] = message.getBuffer();
			}
		}
		return ret;
	}

	public List<SiemensTPDUHeader> readPackage(SiemensDPPackages pack) {
		List<SiemensTPDUHeader> list = new ArrayList<>();
		ComByteMessage readIn = new ComByteMessage();
		boolean finished = false;
		// we have more than one datapoint, so we only have one read request
		byte[] data = pack.getReadyReadMessage();
		if (data == null) {
			return list;
		}
		for (int i = 0; i < pack.getMessageCount(); i++) {
			// set sequence number
			data[11 + i * 31] = (byte) (this.readSequence / 256);
			data[12 + i * 31] = (byte) (this.readSequence % 256);
			this.readSequence++;
			if (this.readSequence > 256 * 256) {
				this.readSequence = 1;
			}
		}
		if (this.write(data, this.readSocket) == -1) {
			// we had an write error, so try to rewrite it
			if (this.connectRead() == ComCommunicationStates.OPEN) {
				if (this.write(data, this.readSocket) == -1) {
					list.clear();
					return list;
				}
			} else {
				list.clear();
				return list;
			}
		}
		long startread = System.nanoTime();
		// now wait for response
		while (!finished) {
			byte[] in = this.read(this.readSocket);
			if (in == null) {
				if (this.connectRead() == ComCommunicationStates.OPEN) {
					in = this.read(this.readSocket);
					if (in == null) {
						list.clear();
						return list;
					}
				}
			}
			if (in != null && in.length > 0) {
				readIn.addBuffer(in);
			}
			if (readIn != null && readIn.getBuffer().length > 0) {
				// try to generate tpdu
				SiemensTPDUHeader tpdu = this.createTPDU(readIn);
				if (tpdu != null) {
					list.add(tpdu);
					/**
					 * we have more than one dp in a single request - response
					 */
					if (list.size() == pack.getMessageCount()) {
						finished = true;
						continue;
					}
				}
			}
			if (startread + (5000L * 1000000L) < System.nanoTime()) {
				list.clear();
				finished = true;
				continue;
			}
		}
		return list;
	}

	public boolean writeMessage(SiemensDPItem dp, DataValue dv) throws ValueOutOfRangeException {
		// how many messages did we need to write a single datapoint
		// we have only one message to send
		byte[][] messages = this.createWriteRequest(dp, dv);
		ComByteMessage readIn = new ComByteMessage();
		boolean finished;
		// we have more than one datapoint, so we only have one read request
		if (messages == null) {
			return false;
		}
		for (byte[] message : messages) {
			if (this.write(message, this.writeSocket) == -1) {
				// we had an write error, so try to rewrite it
				if (this.connectWrite() == ComCommunicationStates.OPEN) {
					if (this.write(message, this.writeSocket) == -1) {
						return false;
					}
				}
			}
			// long startread = System.currentTimeMillis();
			long startread = System.nanoTime();
			finished = false;
			// now wait for response
			while (!finished) {
				byte[] in = this.read(this.writeSocket);
				if (in == null) {
					if (this.connectWrite() == ComCommunicationStates.OPEN) {
						in = this.read(this.writeSocket);
						if (in == null) {
							return false;
						}
					}
				}
				if (in != null && in.length > 0) {
					readIn.addBuffer(in);
				}
				if (readIn != null && readIn.getBuffer().length > 0) {
					// try to generate tpdu
					// SiemensTPDUHeader tpdu = this.createTPDU(readIn);
					// validate tpdu
					finished = true;
					continue;
				}
				// if (startread + 2000 < System.currentTimeMillis()) {
				if (startread + (2000L * 1000000L) < System.nanoTime()) {
					return false;
				}
			}
		}
		return true;
	}

	public byte[] read(Socket socket) {
		byte[] in = new byte[0];
		if (socket != null) {
			try {
				if (socket.getInputStream() != null) {
					int av = socket.getInputStream().available();
					in = new byte[av];
					socket.getInputStream().read(in, 0, in.length);
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				return null;
			}
		}
		return in;
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
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				// we had an write error
				return -1;
			}
		}
		return 0;
	}

	/**
	 * create an read request for the required data point, this method should be
	 * implemented in each different device type
	 * 
	 * @param dp
	 * @return
	 */
	public boolean createSingleReadRequest(SiemensDPItem dp) {
		return false;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setRack(int rack) {
		this.rack = rack;
	}

	public long getDrvId() {
		return drvId;
	}

	public void setDrvId(long drvId) {
		this.drvId = drvId;
	}

	public boolean isInitState() {
		return initState;
	}

	public void setInitState(boolean initState) {
		this.initState = initState;
	}
}
