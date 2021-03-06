package opc.server.hbserver;

import com.bichler.opc.comdrv.ComDRVManager;

import opc.sdk.server.core.UAServerApplicationInstance;

public class OPCUADriverServer extends UAServerApplicationInstance {
	private OPCUADriverConnection driverConnection = null;

	public OPCUADriverServer() {
		super();
	}

	private void initializeAdditionalInformationModels() {
		getServerInstance().importAdditionalModels();
	}

	private void initializeAdditionalNodeSets() {
		getServerInstance().importAdditionalNodeSets();
	}

	public void initializeDriver() {
		OPCUADriverConnection driverConnection = new OPCUADriverConnection(this.getServerInstance());
		this.setDriverConnection(driverConnection);
		this.fetchDriverManager(ComDRVManager.getDRVManager());
		/** fetch the driver manager */
	}

	/**
	 * fetch custom driver manager HB 2017.03.09
	 * 
	 * @param manager
	 */
	public void fetchDriverManager(ComDRVManager manager) {
		/** fetch the driver manager */
		manager.setServer(this.getServerInstance());
		manager.connect(driverConnection);
		manager.loadDrivers();
		manager.startAllDrivers();
	}

	@Override
	public void start() {
		super.initWorkspaceDefaults();
		initializeAdditionalInformationModels();
		initializeAdditionalNodeSets();
		initializeDriver();
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	public void setDriverConnection(OPCUADriverConnection driverConnection) {
		this.driverConnection = driverConnection;
	}

	public OPCUADriverConnection getDriverConnection() {
		return driverConnection;
	}
}
