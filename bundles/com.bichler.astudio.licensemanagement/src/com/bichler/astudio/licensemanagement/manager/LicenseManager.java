package com.bichler.astudio.licensemanagement.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.licensemanagement.util.LicenseUtil;
import com.wibu.cm.CodeMeter;
import com.wibu.cm.CodeMeter.CMACCESS2;
import com.wibu.cm.CodeMeter.CMBOXCONTROL;

public class LicenseManager {

	private Executor executor = Executors.newSingleThreadExecutor();
	private LicenseRunable licenseCheck = null;
	private LicenseCategory license = LicenseCategory.Enterprise;

	private boolean active;

	private long hcmse = 0l;
	private long firmCode = 0l;
	private long productCode = 0l;

	public LicenseManager(boolean active) {
		// LicenseCategory license = readLicense();
		// this.currentLicense = license;
		// boolean inited = CodeMeter.InitLib();
		// String text = CodeMeter.cmGetLastErrorText();
		// System.out.println(inited);
		this.active = active;
	}

	public void stop() {
		if (this.hcmse != 0l) {
			CodeMeter.cmRelease(this.hcmse);
		}

		if (this.licenseCheck != null) {
			this.licenseCheck.stop();
			this.licenseCheck = null;
		}
	}

	public long accessLicense(long firmCode, long productCode) {
		CMACCESS2 cmacc = new CMACCESS2();
		// cmacc.ctrl = CodeMeter.CM_ACCESS_CONVENIENT;
		cmacc.firmCode = firmCode;
		cmacc.productCode = productCode;
		long hcmse = CodeMeter.cmAccess2(CodeMeter.CM_ACCESS_LOCAL, cmacc);
		if (0 == hcmse) {
			System.out.println("CmAccess() failed with error: " + CodeMeter.cmGetLastErrorText());
		}

		return hcmse;
	}

	public long releaseLicense() {
		if (this.hcmse != 0) {
			CodeMeter.cmRelease(hcmse);
			hcmse = 0;
		}

		return hcmse;
	}

	public LicenseCategory getLicense() {
		return this.license;
	}

	public boolean isActive() {
		return this.active;
	}

	// public long getHcmse(){
	// return this.hcmse;
	// }
	/**
	 * Call only once!
	 * 
	 * @param hcmse
	 */
	public void setHcmse(long firmCode, long productCode, long hcmse) {
		this.firmCode = firmCode;
		this.productCode = productCode;
		this.hcmse = hcmse;

		// get license
		if (this.active) {
			// hcmse = 0; no license
			if (hcmse == 0) {
				this.license = LicenseCategory.Evaluation;
			} else if (productCode == 1001) {
				this.license = LicenseCategory.Enterprise;
			} else if (productCode == 1002) {
				this.license = LicenseCategory.Academic;
			}

			this.start();
		} else {
			this.license = LicenseCategory.Enterprise;
		}

		this.license.setActive(this.active);
	}

	private void checkLicenseExpired() throws ASStudioLicenseException {
		if (this.license == null) {
			throw new ASStudioLicenseException("Cannot start CometStudio One with no license!");
		}

		switch (this.license) {
		case Evaluation:
			break;
		case Academic:
		case Enterprise:
			CMBOXCONTROL cmversion = new CMBOXCONTROL();
			boolean success = CodeMeter.cmGetInfo(this.hcmse, CodeMeter.CM_GEI_VERSION, cmversion);

			if (!success) {
				long hcmse = LicenseUtil.openLicenseNoCmStickConnection(this.firmCode, this.productCode);
				if (hcmse == 0) {
					throw new ASStudioLicenseException("Cannot start CometStudio One with no license!");
				}

				this.hcmse = hcmse;
			}

			break;

		}
	}

	private void start() {
		this.licenseCheck = new LicenseRunable();
		this.executor.execute(this.licenseCheck);
	}

	class LicenseRunable implements Runnable {
		// 10 secs
		private static final long TIMEOUT = 10000l;

		private boolean running;

		public LicenseRunable() {
			this.running = true;
		}

		@Override
		public void run() {

			while (this.running) {

				try {
					checkLicenseExpired();
					try {
						Thread.sleep(TIMEOUT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (final ASStudioLicenseException e1) {
					e1.printStackTrace();

					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							// LicenseWizardUtil.openLicenseValueExceeded(license.name()
							// + " - License", "License expired",
							// "License is expired and needs to be renewed!");
							System.exit(-1);
						}
					});

				}

			}
		}

		public void stop() {
			this.running = false;
		}
	}

}
