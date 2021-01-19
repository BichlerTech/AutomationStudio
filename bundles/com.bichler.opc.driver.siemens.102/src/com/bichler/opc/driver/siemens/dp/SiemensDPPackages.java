package com.bichler.opc.driver.siemens.dp;

import java.util.ArrayList;
import java.util.List;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.SiemensTCPISODevice;

/**
 * class reads a package of data points at once, for that read we get only one
 * message back
 * 
 * @author HB-Softsolution e.U.
 *
 */
public class SiemensDPPackages {
	/**
	 * indicate if we have a package with one datapointvalue to read or a combined
	 * one
	 */
	private boolean singleDP = false;
	private List<SiemensDPItem> dps = new ArrayList<>();
	private byte[] readyReadMessage = null;
	// private byte []readyWriteMessage = null;
	private int messageCount = 1;
	// private List<SiemensWriteRequest> wrequ = new
	// ArrayList<SiemensWriteRequest>();
	private int responseCount = 0;

	public List<SiemensDPItem> getDps() {
		return dps;
	}

	public void setDps(List<SiemensDPItem> dps) {
		this.dps = dps;
	}

	/**
	 * create a ready built read request, so we only have to set the correct
	 * sequence number to message and then send it to plc
	 */
	public void createReadMessage(SiemensTCPISODevice device) {
		ComByteMessage message = new ComByteMessage();
		this.messageCount = 1;
		int overhead = 4;
		if (this.dps.size() == 1) {
			messageCount = this.dps.get(0).getReadMessageCount();
			setResponseCount((dps.get(0).getLength() * dps.get(0).getReadArraylength(-1)) + overhead);
		} else {
			for (SiemensDPItem dp : dps) {
				setResponseCount(getResponseCount() + ((dp.getLength() * dp.getReadArraylength(-1)) + overhead));
			}
		}
		for (int i = 0; i < messageCount; i++) {
			device.addISOTCPHeader(message, (byte) (19 + this.dps.size() * 12));
			device.addTPDUHeader(message);
			device.addReadPDU(message, 2 + this.dps.size() * 12);
			device.addReadParameter(message, (byte) this.dps.size());
			device.addReadData(message, this.dps, i);
		}
		this.readyReadMessage = message.getBuffer();
	}

	/**
	 * create a ready built write request, so we only have to set the correct
	 * sequence number to message and then send it to plc
	 *
	 * public void createWriteMessage(SiemensTCPISODevice device) {
	 * 
	 * ComByteMessage message = new ComByteMessage();
	 * 
	 * this.messageCount = 1; if(this.wrequ.size() == 1) { messageCount =
	 * this.wrequ.get(0).getDp().getMessageCount(); }
	 * 
	 * for(int i = 0; i < messageCount; i++) { int length = 17; int paramlength = 2;
	 * int datalength = 0; for(SiemensWriteRequest requ : this.wrequ) { paramlength
	 * += 12; datalength += 4 +
	 * requ.getDp().getLength()*requ.getDp().getArraylength(-1);
	 * 
	 * } device.addISOTCPHeader(message, (byte) (length + paramlength +
	 * datalength));
	 * 
	 * device.addTPDUHeader(message);
	 * 
	 * device.addWritePDU(message, paramlength, datalength);
	 * 
	 * device.addWriteParameter(message, wrequ);
	 * 
	 * device.addWriteData(message, wrequ); }
	 * 
	 * this.readyWriteMessage = message.getBuffer(); }
	 */
	/**
	 * get ready build message to send it to device, we only have to set the correct
	 * sequence number to message an then send it to plc
	 * 
	 * @return
	 */
	public byte[] getReadyReadMessage() {
		return readyReadMessage;
	}

	/**
	 * get ready build message to send it to device, we only have to set the correct
	 * sequence number to message an then send it to plc
	 * 
	 * @return
	 */
	// public byte [] getReadyWriteMessage() {
	// return readyWriteMessage;
	// }
	// public void addWriteRequest(SiemensDPItem item, DataValue value) {
	// SiemensWriteRequest requ = new SiemensWriteRequest();
	// requ.setDp(item);
	// requ.setValue(value);
	//
	// this.wrequ.add(requ);
	// }
	//
	// public void addWriteRequest(SiemensWriteRequest requ) {
	// this.wrequ.add(requ);
	// }
	public void addDP(SiemensDPItem dp) {
		this.dps.add(dp);
	}

	public boolean isSingleDP() {
		return singleDP;
	}

	public void setSingleDP(boolean singleDP) {
		this.singleDP = singleDP;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(int responseCount) {
		this.responseCount = responseCount;
	}
}
