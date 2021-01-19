package com.bichler.astudio.opcua.components.ui.dialogs;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.MonitoringParameters;
import org.opcfoundation.ua.core.TimestampsToReturn;

// TODO: Auto-generated Javadoc
/**
 * The Class DriverField.
 */
public class OPCUANodeField{

	String 	server;
	String 	browsepath;
	String 	namespace;
	String 	id;
	
	/** The data point.
	NodeId nodeid;
	
	
	
	String 	id;
	int 	sInterval;
	boolean	disco;
	int	 	qusize;
	String 	filter;
	String 	deadbandt;
	double 	deadbandv;
	String 	trigger;
	String	indexr;
	String 	dataenc;
	String 	timestret;
	*/
	
	OPCUaDataChangeParameters params 			= new OPCUaDataChangeParameters();
	MonitoringParameters monitoringParams 	= new MonitoringParameters();

	double samplingInterval 		= 1000d;
	boolean discardOldest 			= true;
	UnsignedInteger queueSize 		= new UnsignedInteger(10);
	boolean enableFilter 			= false;
	String deadbandT 				= "";
	double deadbandV 				= 10;
	String trigger 					= "";
	String indexR 					= "";
	String dataEnc 					= "";
	MonitoringMode mode 			= MonitoringMode.Disabled;
	TimestampsToReturn timetoret 	= TimestampsToReturn.Neither;

	/** monitoring parameters */
	OPCUaDataChangeParameters changeParams = new OPCUaDataChangeParameters();
	
	/**
	 * Checks if is valid.
	 * 
	 * @return true, if is valid
	 */
	public boolean isValid() {
		if(getBrowsepath() != null){
			return true;
		}
		return false;
	}	
	
	public OPCUaDataChangeParameters getDataChangeParams() {
		return this.params;
	}

	public void setDataChangeParams(OPCUaDataChangeParameters params) {
		this.params = params;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	public String getBrowsepath() {
		return browsepath;
	}

	public void setBrowsepath(String browsepath) {
		this.browsepath = browsepath;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MonitoringParameters getMonitoringParams() {
		return this.monitoringParams;
	}

	public void setMonitoringParams(MonitoringParameters monitoringParams) {
		this.monitoringParams = monitoringParams;
	}
}
