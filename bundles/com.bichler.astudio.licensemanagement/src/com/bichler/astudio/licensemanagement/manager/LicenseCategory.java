package com.bichler.astudio.licensemanagement.manager;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.licensemanagement.util.LicenseUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public enum LicenseCategory {
	Evaluation(1, 1, 1, 1, 100, 1), Academic(0, 0, 0, 0, 0, 0), Enterprise(0, 0, 0, 0, 0, -1);

	// private static final int DEFAULT_COUNT_NAMESPACES = 3;
	private static final int DEFAULT_COUNT_NODES = 2050000;
	// modelierung
	private final int MAX_DRIVERS;
	private final int MAX_MODULES;
	private final int MAX_HMI_SERVER;
	private final int MAX_OPCUA_SERVER;
	private final int MAX_SCRIPTS;
	// private final int MAX_NAMESPACE;
	private final int MAX_NODES;

	private int current_hmi_server;
	private int current_opcua_server;
	private int current_driver;
	private int current_module;
	// private int current_namespace;
	private int current_nodes;
	private boolean active = true;
	// upload
	// private boolean licenseUpload2Server;
	// dialog
	// private boolean showInformation;
	// private String information;
	// expired date
	// private long expired = DateTime.currentTime().getTimeInMillis() +
	// 1000000000;
  private int current_scripts;

	LicenseCategory(int maxOpcuaServer, int maxHmiServer, int maxDrivers, int maxModules, int maxNodes, int maxScripts) {

		if (maxNodes == 0) {
			this.MAX_NODES = maxNodes;
		} else {
			this.MAX_NODES = DEFAULT_COUNT_NODES + maxNodes;
		}
		this.MAX_DRIVERS = maxDrivers;
		this.MAX_MODULES = maxModules;
		this.MAX_HMI_SERVER = maxHmiServer;
		this.MAX_OPCUA_SERVER = maxOpcuaServer;
		this.MAX_SCRIPTS = maxScripts;
		// this.licenseUpload2Server = licenseUploadedServer;

		// this.showInformation = false;
		// this.information = "";

		initServer();
		initDriver();
		initOPC();

	}

	private void initDriver() {
		this.current_driver = 0;
	}

	private void initOPC() {
		// this.current_namespace = 0;
		this.current_nodes = 0;
	}

	private void initServer() {
		this.current_opcua_server = 0;
		this.current_hmi_server = 0;
	}

	// LicenseCategory(int maxOpcuaServer, int maxHmiServer, int maxDrivers, int
	// maxNodes, String information) {
	// this(maxOpcuaServer, maxHmiServer, maxDrivers, maxNodes);
	//
	// this.showInformation = true;
	// this.information = information;
	// }

	public void startDriver(int driverCount) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}

		initDriver();
		try {
			validateAddDriver(driverCount, true);
		} catch (ASStudioLicenseException e) {
			this.current_driver = driverCount;
			e.printStackTrace();

			LicenseUtil.openLicenseErrorStartup(getName(),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.driver"),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.driver"));
			throw e;
		}
	}

	public void startOPC(int nodeCount, int namespaceCount) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		initOPC();
		boolean error = false;

		// try {
		// validateAddNamespace(namespaceCount, true);
		// } catch (CometStudioLicenseException e) {
		// this.current_namespace = namespaceCount;
		// e.printStackTrace();
		// error = true;
		// }
		try {
			validateAddNodes(nodeCount, true);
		} catch (ASStudioLicenseException e) {
			this.current_nodes = nodeCount;
			e.printStackTrace();
			error = true;
		}

		if (error) {
			LicenseUtil.openLicenseErrorStartup(getName(),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.nodes"),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.nodes"));
			throw new ASStudioLicenseException("Shutdown opc ua!");
		}
	}

	public void startServerProjects(int opcserverCount, int hmiserverCount) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		initServer();
		boolean error = false;
		try {
			validateAddOpcuaServer(opcserverCount, true);
		} catch (ASStudioLicenseException e) {
			this.current_opcua_server = opcserverCount;
			e.printStackTrace();
			error = true;
		}
		try {
			validateAddHmiServer(hmiserverCount, true);
		} catch (ASStudioLicenseException e) {
			this.current_hmi_server = hmiserverCount;
			e.printStackTrace();
			error = true;
		}

		if (error) {
			LicenseUtil.openLicenseErrorStartup(getName(),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.projects"),
					CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.projects"));
			throw new ASStudioLicenseException("To many server projectes!");
		}
	}

//	public void stopOPC() {
//		if (!this.active) {
//			return;
//		}
//	}

	// public void setExpired(long expired) {
	// this.expired = expired;
	// }

	// public void checkExpired() throws CometStudioLicenseException {
	// long result = this.expired - DateTime.currentTime().getTimeInMillis();
	//
	// if (result <= 0) {
	// throw new CometStudioLicenseException("Cannot start CometStudio One with
	// expired license!");
	// }
	// }

	public void validateAddDriver(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_DRIVERS <= 0) {
			return;
		}

		if (this.current_driver + count > this.MAX_DRIVERS) {
			if (!onStartup) {
				LicenseUtil.openLicenseValueExceeded(getName(),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.driver"),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.driver"));
			}
			throw new ASStudioLicenseException(
					"Cannot add more namespace entries! License " + name() + " do not support any more namespaces!");
		}

		this.current_driver += count;
	}
	
	public void validateAddModule(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_MODULES <= 0) {
			return;
		}

		if (this.current_module + count > this.MAX_MODULES) {
			if (!onStartup) {
				LicenseUtil.openLicenseValueExceeded(getName(),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.module"),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.module"));
			}
			throw new ASStudioLicenseException(
					"Cannot add more namespace entries! License " + name() + " do not support any more namespaces!");
		}

		this.current_module += count;
	}


	public void validateAddHmiServer(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_HMI_SERVER <= 0) {
			return;
		}

		if (this.current_hmi_server + count > this.MAX_HMI_SERVER) {
			if (!onStartup) {
				LicenseUtil.openLicenseValueExceeded(getName(),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.hmiservers"),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.hmiservers"));
			}
			throw new ASStudioLicenseException("Cannot add more opc ua server procets! License " + name()
					+ " do not support any more opc ua server projects!");
		}

		this.current_hmi_server += count;
	}

	public void validateAddNodes(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_NODES <= 0) {
			return;
		}

		if (this.current_nodes + count > this.MAX_NODES) {
			if (!onStartup) {
				LicenseUtil.openLicenseValueExceeded(getName(),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.nodes"),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.limit.nodes"));
			}
			throw new ASStudioLicenseException(
					"Cannot add more nodes! License " + name() + " do not support any more nodes!");
		}

		this.current_nodes += count;
	}

	public void validateAddOpcuaServer(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_OPCUA_SERVER <= 0) {
			return;
		}

		if (this.current_opcua_server + count > this.MAX_OPCUA_SERVER) {
			if (!onStartup) {
				LicenseUtil.openLicenseValueExceeded(getName(),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.opcuaservers"),
						CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE,
								"form.limit.opcuaservers"));
			}
			throw new ASStudioLicenseException("Cannot add more opc ua server projects! License " + name()
					+ " do not support any more opc ua server projects!");
		}

		this.current_opcua_server += count;
	}

	public void validateDeleteHmiServer(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_HMI_SERVER <= 0) {
			return;
		}

		if (this.current_hmi_server - count < 0) {
			if (!onStartup) {
				// LicenseWizardUtil.openLicenseValueExceeded(onStartup);
			}
			throw new ASStudioLicenseException("IndexOutOfRange!");
		}

		this.current_hmi_server -= count;
	}

	public void validateAddEcmaScript(int count, boolean onStartup)  throws ASStudioLicenseException {
    if (!this.active) {
      return;
    }
    
    // no limit
    if (this.MAX_SCRIPTS < 0) {
      return;
    }

    if (this.current_scripts - count < 0) {
      if (!onStartup) {
        // LicenseWizardUtil.openLicenseValueExceeded(onStartup);
      }
      throw new ASStudioLicenseException("IndexOutOfRange");
    }

    this.current_scripts -= count;
  }
	
	public void validateDeleteDriver(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_DRIVERS <= 0) {
			return;
		}

		if (this.current_driver - count < 0) {
			if (!onStartup) {
				// LicenseWizardUtil.openLicenseValueExceeded(onStartup);
			}
			throw new ASStudioLicenseException("IndexOutOfRange");
		}

		this.current_driver -= count;
	}

	public void validateDeleteNodes(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_NODES <= 0) {
			return;
		}

		if (this.current_nodes - count < DEFAULT_COUNT_NODES) {
			if (!onStartup) {
				// LicenseWizardUtil.openLicenseValueExceeded(onStartup, "Opc Ua
				// Addressspace");
			}
			throw new ASStudioLicenseException("Cannot delete less nodes than OPC UA DEFAULT!");
		}

		this.current_nodes -= count;
	}

	public void validateDeleteOpcuaServer(int count, boolean onStartup) throws ASStudioLicenseException {
		if (!this.active) {
			return;
		}
		
		// no limit
		if (this.MAX_OPCUA_SERVER <= 0) {
			return;
		}

		if (this.current_opcua_server - count < 0) {
			if (!onStartup) {
				// LicenseWizardUtil.openLicenseValueExceeded(onStartup);
			}
			throw new ASStudioLicenseException("IndexOutOfRange!");
		}

		this.current_opcua_server -= count;
	}

	public String getName() {
		return CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, name());
	}

	public boolean isActive(){
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

}
