package com.bichler.opc.driver.xml_da;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis.AxisFault;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.webservices.XMLDA._1_0.ItemValue;
import org.opcfoundation.webservices.XMLDA._1_0.OPCError;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList;
import org.opcfoundation.webservices.XMLDA._1_0.ReadResponse;
import org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList;
import org.opcfoundation.webservices.XMLDA._1_0.RequestOptions;
import org.opcfoundation.webservices.XMLDA._1_0.ServiceStub;

import com.bichler.opc.comdrv.AComDevice;
import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;

/**
 * This class represents the hardware connection to the xml da device.
 * 
 * @author hannes bichler
 * @company hb-softsolution
 * @version 1.0.1
 * @contact h.bichler@hb-softsolution.com
 * 
 */
public class XML_DA_Device extends AComDevice {
	/**
	 * service stub to xml-da
	 */
	private ServiceStub service = null;
	/**
	 * server url of xml da server
	 */
	private String xml_da_server_url = "";
	/**
	 * user name to connect to xml-da server
	 */
	private String xml_da_username = "";
	/**
	 * password to connetct to xml-da server
	 */
	private String xml_da_password = "";
	/**
	 * timeout to connect to xml-da server
	 */
	private int xml_da_connecttimeout = 1000;
	private int max_dp_count = -1;
	private int max_scalar_dp_count = -1;
	private int max_array_dp_count = -1;
	/**
	 * resource manager with all internal elements like maps of dps, which are
	 * connected to the xml-da server.
	 */
	private XML_DA_ResourceManager manager;

	/**
	 * 
	 * @return
	 */
	public int getXml_da_connecttimeout() {
		return xml_da_connecttimeout;
	}

	/**
	 * Set xml da connection timeout and also the reconnect tiemout, between each
	 * reconnect schedule if connection is lost.
	 * 
	 * @param xml_da_connecttimeout
	 */
	public void setXml_da_connecttimeout(int xml_da_connecttimeout) {
		this.xml_da_connecttimeout = xml_da_connecttimeout;
		this.reconnectTimeout = xml_da_connecttimeout;
	}

	@Override
	public long getDeviceState() {
		return this.deviceState;
	}

	/**
	 * the connect method creates a new xml-da serivce object with url, username,
	 * password and timeout for later use.
	 */
	@Override
	public long connect() {
		try {
			service = new ServiceStub(new URL(xml_da_server_url), null);
			service.setUsername(xml_da_username);
			service.setPassword(xml_da_password);
			// set timeout in milli
			service.setTimeout(xml_da_connecttimeout);
			this.deviceState = ComCommunicationStates.OPEN;
		} catch (AxisFault afx) {
			this.deviceState = ComCommunicationStates.CLOSED;
		} catch (MalformedURLException me) {
			// this.logger.error1("Server URL is not well formed! Serverurl: "
			// + this.xml_da_server_url, CometModuls.STR_DRV,
			// CometModuls.INT_DRV, CometDRV.BUNDLEID, CometDRV.VERSIONID);
			this.deviceState = ComCommunicationStates.CLOSED;
		}
		return this.deviceState;
	}

	@Override
	public long reconnect() {
		this.deviceState = ComCommunicationStates.OPEN;
		return super.reconnect();
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		super.disconnect();
	}

	public String getXml_da_server_url() {
		return xml_da_server_url;
	}

	public void setXml_da_server_url(String xml_da_server_url) {
		this.xml_da_server_url = xml_da_server_url;
	}

	public String getXml_da_username() {
		return xml_da_username;
	}

	public void setXml_da_username(String xml_da_username) {
		this.xml_da_username = xml_da_username;
	}

	public String getXml_da_password() {
		return xml_da_password;
	}

	public void setXml_da_password(String xml_da_password) {
		this.xml_da_password = xml_da_password;
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

	public void storeWriteReq(ComDP dp) {
		// TODO Auto-generated method stub
	}

	public void write(String itemPath, String itemName, Object value) {
		org.opcfoundation.webservices.XMLDA._1_0.Write parameters = new org.opcfoundation.webservices.XMLDA._1_0.Write();
		org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
		org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList();
		options.setLocaleID("en");
		parameters.setOptions(options);
		org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items = new ItemValue[1];
		org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = new ItemValue();
		item.setItemPath(itemPath);
		item.setItemName(itemName);
		// item.setValueTypeQualifier(new
		// javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
		// "string")); //"int"));
		// String str = new String("" + value);
		item.setValue(value);
		// item.setClientItemHandle("j");
		items[0] = item;
		itemList.setItems(items);
		parameters.setItemList(itemList);
		org.opcfoundation.webservices.XMLDA._1_0.WriteResponse writeResponse = null;
		try {
			writeResponse = service.write(parameters);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (writeResponse != null) {
			System.out.println("writeResponse == null");
		} else {
			System.out.println("writeResponse == null");
		}
	}

	/**
	 * registers all nodes to xml da server to read it later every cycle
	 */
	public void registerNodes() {
		// first register all cyclic nodes
		// this.registerCyclicNodes();
		// now register all trigger nodes
		// this.registerTriggerNodes();
	}

	public boolean readScalarNodes() {
		boolean worked = false;
		if (this.manager.getScalarNodes() != null) {
			for (long timeout : this.manager.getScalarNodes().keySet()) {
				// verify timeout
				XML_DA_ItemList<XML_DA_DPItem> itemlist = this.manager.getScalarNodes().get(timeout);
				if (itemlist != null) {
					long now = System.currentTimeMillis();
					if (itemlist.getLastRead() + timeout < now) {
						itemlist.setLastRead(now);
						worked = this.readNodes(itemlist, max_scalar_dp_count) || worked;
					}
				}
			}
		}
		return worked;
	}

	public boolean readArrayNodes() {
		boolean worked = false;
		if (this.manager.getArraynodes() != null) {
			for (long timeout : this.manager.getArraynodes().keySet()) {
				// verify timeout
				XML_DA_ItemList<XML_DA_DPItem> itemlist = this.manager.getArraynodes().get(timeout);
				if (itemlist != null) {
					long now = System.currentTimeMillis();
					if (itemlist.getLastRead() + timeout < now) {
						itemlist.setLastRead(now);
						worked = this.readNodes(itemlist, max_array_dp_count) || worked;
					}
				}
			}
		}
		return worked;
	}

	/**
	 * read
	 */
	public boolean readNodes(List<XML_DA_DPItem> nodes, int count) {
		boolean worked = false;
		RequestOptions options = new RequestOptions();
		ReadRequestItemList itemList = new ReadRequestItemList();
		ReadRequestItem[] requestItems = null;// new ReadRequestItem[1];
		ReadRequestItem item = null;
		XML_DA_DPItem node = null;
		// calculate itemcount per read
		int maxcount = nodes.size();
		if (max_dp_count > -1)
			maxcount = Math.min(maxcount, max_dp_count);
		if (count > -1)
			maxcount = Math.min(maxcount, count);
		requestItems = new ReadRequestItem[maxcount];
		ReadResponse readresponse = null;
		options.setLocaleID("en");
		options.setReturnItemName(true);
		options.setReturnItemPath(true);
		int index = 0;
		// first we read all scalar nodes
		for (int i = 0; i < nodes.size(); i++) {
			item = new ReadRequestItem();
			node = nodes.get(i);
			item.setItemName(node.getItemName());
			item.setItemPath(node.getItemPath());
			requestItems[index] = item;
			index++;
			// did we reach max datapoints per read
			if ((index) % maxcount == 0 || (i + 1) == nodes.size()) {
				itemList.setItems(requestItems);
				try {
					readresponse = service.read(options, itemList);
					worked = true;
					// now try to evaluate read response
					if (readresponse != null) {
						// first validate error messages
						if (readresponse.getErrors() != null) {
							for (OPCError error : readresponse.getErrors()) {
								if (error != null) {
									// this.logger.error1(
									// "We got error message from xml-da server! message: "
									// + error.getID()
									// .getLocalPart(),
									// CometModuls.STR_DRV,
									// CometModuls.INT_DRV,
									// CometDRV.BUNDLEID,
									// CometDRV.VERSIONID);
								}
							}
						}
						// now set all values to opc ua server
						if (readresponse.getRItemList() != null) {
							ReplyItemList itemlist = readresponse.getRItemList();
							for (ItemValue val : itemlist.getItems()) {
								if (val != null) {
									// find all data points which should be
									// updated
									List<XML_DA_DPItem> dps = manager.getDpByHandle()
											.get(val.getItemPath() + "/" + val.getItemName());
									if (dps != null) {
										if (val != null && val.getValue() != null) {
											for (XML_DA_DPItem dp : dps) {
												DataValue dv = dp.drv2Prog(val.getValue());
												if (dp.getNodeId() != null) {
													// first check if node is
													// trigger node, so write it
													// to address space ,
													// if not, add it only into
													// internal address space
													StatusCode[] code = null;
													if (manager.getTriggerList() != null && manager.getTriggerList()
															.containsKey(dp.getNodeId().toString())) {
														code = manager.getDrvManager().writeFromDriver(
																new NodeId[] { dp.getNodeId() },
																new UnsignedInteger[] { Attributes.Value },
																new String[] { null }, new DataValue[] { dv },
																new Long[] { (long) 0 });
													} else {
														code = manager.getDrvManager().writeFromDriver(
																new NodeId[] { dp.getNodeId() },
																new UnsignedInteger[] { Attributes.Value }, null, // new
																// String[]{""},
																new DataValue[] { dv }, new Long[] { (long) 0 });
													}
													if (code != null && code[0] != null && code[0].isBad()) {
														// this.logger
														// .error1("Couldn't write value to opc ua address
														// space! node: ["
														// + dp.getDisplayname()
														// + "] code: "
														// +
														// code.getDescription()
														// +
														// " datatype provided: "
														// + val.getValue()
														// .getClass()
														// .getSimpleName(),
														// CometModuls.STR_DRV,
														// CometModuls.INT_DRV,
														// CometDRV.BUNDLEID,
														// CometDRV.VERSIONID);
													}
												}
											}
										}
									}
								}
							}
						}
					} else {
					}
				} catch (RemoteException e) {
					this.deviceState = ComCommunicationStates.CLOSED;
					// this.logger.error1(
					// "We couldn't connect to xml-da server! Serverurl: "
					// + this.xml_da_server_url,
					// CometModuls.STR_DRV, CometModuls.INT_DRV,
					// CometDRV.BUNDLEID, CometDRV.VERSIONID);
				}
				if ((i + 1) < nodes.size()) {
					// we need a new read request
					requestItems = new ReadRequestItem[Math.min(maxcount, nodes.size() - i - 1)];
					index = 0;
				}
			}
		}
		return worked;
	}

	public void setResouceManager(XML_DA_ResourceManager manager) {
		this.manager = manager;
	}

	public int getMaxDPCount() {
		return max_dp_count;
	}

	public void setMaxDPCount(int max_dp_count) {
		this.max_dp_count = max_dp_count;
	}

	public int getMaxScalarDPCount() {
		return max_scalar_dp_count;
	}

	public void setMaxScalarDPCount(int max_scalar_dp_count) {
		this.max_scalar_dp_count = max_scalar_dp_count;
	}

	public int getMaxArrayDPCount() {
		return max_array_dp_count;
	}

	public void setMaxArrayDPCount(int max_array_dp_count) {
		this.max_array_dp_count = max_array_dp_count;
	}
}
