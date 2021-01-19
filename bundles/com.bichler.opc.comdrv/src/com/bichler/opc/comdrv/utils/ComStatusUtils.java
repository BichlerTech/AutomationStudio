package com.bichler.opc.comdrv.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComResourceManager;

public class ComStatusUtils {
	private ComResourceManager manager = ComDRVManager.getDRVManager().getResourceManager();
	public static final String NOACTIVITY = "0";
	public static final String STARTING = "1";
	public static final String RUNNING = "2";
	public static final String ERROR = "3";

	public void writeLEDStatus(String status) {
		if (new File("/cometintern").exists()) {							// write state only on embedded hardware
			File state = new File("/cometintern/watchdog/server.state");
			if (!state.exists()) {
				try {
					@SuppressWarnings("unused")
					boolean created = state.createNewFile();
					if (created && manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "File for LED Status created!");
					}
				} catch (IOException e) {
					if (manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "LED Status | " + e.getMessage());
					}
				}
			}
			if (state.exists()) {
				try (FileWriter fr = new FileWriter(state);) {
					fr.write(status);
					fr.flush();
				} catch (Exception e) {
					if (manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "LED Status | " + e.getMessage());
					}
				}
			}
		}
	}

	public void updateWatchdog() {
		if (new File("/cometintern").exists()) {							// write watchdog only on embedded hardware
			File wd = new File("/cometintern/watchdog/cometsrvwd");
			if (!wd.exists()) {
				try {
					@SuppressWarnings("unused")
					boolean created = wd.createNewFile();
					if (created && manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "File for watchdog created!");
					}
				} catch (IOException e) {
					if (manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Watchdog | " + e.getMessage());
					}
				}
			}
			if (wd.exists()) {
				try (FileWriter fr = new FileWriter(wd);) {
					fr.write(1);
					fr.flush();
				} catch (Exception e) {
					if (manager.isActivatedebug()) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Watchdog | " + e.getMessage());
					}
				}
			}
		}
	}
}
