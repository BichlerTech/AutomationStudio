// (c) Copyright 2010 HB-Softsolution e.U.
// ALL RIGHTS RESERVED.
//
// DISCLAIMER:
// This code is provided and developed by HB-Softsolution e.U.
// Distribution of this source code underlies the Warranty
// and Liability Disclaimers which appear in the
// HB-Softsolution e.U. license agreements.
//
// Authors: hannes bichler
// Company: HB-Softsolution e.U.
// Web: www.hb-softsoluiton.com
// contact: hannes.bichler@hb-softsolution.com
package com.bichler.opc.comdrv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class schedules all data points which are configured to read value each
 * interval and pass it to destination data point
 * 
 * the schedule task runs in an own thread
 * 
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2009
 * 
 */
public class CometScheduler extends Thread {
	/***************************************************************************
	 * 
	 * region private fields
	 * 
	 **************************************************************************/
	/**
	 * thread which schedules all configured data points
	 */
	private CometScheduler thread = null;
	/**
	 * flag indicate that the schedule thread should read values
	 */
	private volatile boolean run_flag = false;
	/**
	 * flag indicate that the schedule thread should stop
	 */
	private volatile boolean stop_flag = false;
	/**
	 * flag indicate that the schedule thread is stopped
	 */
	private volatile boolean is_stopped = false;
	private Logger logger = Logger.getLogger(getClass().getName());
	private ComDRVManager drvManager = null;

	/**
	 * default constructor which initialize parent drv object
	 */
	public CometScheduler() {
		this.drvManager = ComDRVManager.getDRVManager();
	}

	/***************************************************************************
	 * 
	 * end region private fields
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region scheduler control functions
	 * 
	 **************************************************************************/
	/**
	 * initial schedule thread and start it
	 * 
	 * @return always true
	 */
	public boolean startschedule() {
		// start schedule thread
		if (this.thread == null) {
			this.thread = new CometScheduler();
		}
		// set runflag true
		this.thread.run_flag = true;
		// set stopflag false
		this.thread.stop_flag = false;
		// get thread state
		// if it isn't started so start it
		if (this.thread.getState() == State.NEW) {
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.info("Scheduler successfully started!");
			}
			this.thread.start();
		}
		return true;
	}

	/**
	 * stops the schedule thread thread is still running to destroy thread call
	 * exitthread
	 */
	public void stopschedule() {
		if (this.thread != null) {
			this.thread.run_flag = false;
		}
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
						.getResourceManager().DEBUG_STARTUP) {
			logger.info("Scheduler successfully stopped!");
		}
	}

	/**
	 * stops the schedule thread and wait for exiting
	 */
	public void exitschedule() {
		this.thread.stop_flag = true;
		// thread is stopped
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
						.getResourceManager().DEBUG_STARTUP) {
			logger.info("Stopping scheduler!");
		}
		// wait for end of schedule thread
		while (!this.thread.is_stopped) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		// destroy thread object
		this.thread = null;
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
						.getResourceManager().DEBUG_STARTUP) {
			logger.info("Scheduler successfully exited!");
		}
	}

	/***************************************************************************
	 * 
	 * end region scheduler control functions
	 * 
	 **************************************************************************/
	/**
	 * working rountine to run each driver in loop we schedule internal loop by 10
	 * milli, if a driver will be scheduled without sleep of 50 milli, so we wait
	 * for that
	 */
	public void run() {
		/* flag indicate that any driver has done any read write action */
		Map<Long, ComDRV> drivers = this.drvManager.getDrivers();
		// first create list with all drivers to schedule
		List<ComDRV> list2Schedule = new ArrayList<>();
		for (ComDRV drv : drivers.values()) {
			if (drv.isNeedSchedule()) {
				list2Schedule.add(drv);
			}
		}
		// run schedule loop until it should stop
		while (!this.stop_flag) {
			if (this.run_flag) {
				if (this.drvManager.getResourceManager().getLastdebugread() + 1000L * 1000000L * 60L < System
						.nanoTime()) {
					this.drvManager.readDebug();
					/**
					 * fill version if exists
					 */
					this.drvManager.fillVersions();
					/**
					 * set time of last debug read
					 */
					this.drvManager.getResourceManager().setLastdebugread(System.nanoTime());
				}
				if (list2Schedule.isEmpty()) {
					// we need a timeout if we have no driver to scedule
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
				for (ComDRV drv : list2Schedule) {
					if (drv.getState() == ComDRVStates.STARTING) {
						// the driver is starting so do not any other things
						// with it
					} else if (drv.getDeviceState() == ComCommunicationStates.CLOSED) {
						if ((drv.getDevice().getReconnectTimeout() * 1000000L)
								+ drv.getDevice().getLastReconnect() < System.nanoTime()) {
							// now we try to reconnect
							drv.getDevice().setLastReconnect(System.nanoTime());
							drv.getDevice().reconnect();
						}
					} else if (drv.getState() == ComDRVStates.STOPPED) {
						// driver has stopped
						continue;
					} else if (drv.getLastRun() + drv.getScheduleNano() < System.nanoTime()) {
						long starttime = System.nanoTime();
						long sleepinterval = 0;
						drv.doWork();
						long endtime = System.nanoTime();
						sleepinterval = drv.getScheduleNano() - (endtime - starttime);
						if (sleepinterval < ComConfig.SCHEDULEINTERVAL) {
							sleepinterval = ComConfig.SCHEDULEINTERVAL;
							if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager
									.getResourceManager().getDebug()
									& this.drvManager.getResourceManager().DEBUG_READINTERVAL) == this.drvManager
											.getResourceManager().DEBUG_READINTERVAL) {
								logger.info(
										"Driver " + drv.getDriverName() + " require " + drv.getScheduleNano() / 1000000L
												+ " - (" + ComConfig.SCHEDULEINTERVAL / 1000000L + "), but it takes "
												+ (endtime - starttime) / 1000000L + " , so wait for minimum "
												+ ComConfig.SCHEDULEINTERVAL / 1000000L + " milliseconds");
							}
						}
						drv.setLastRun(System.nanoTime());
						try {
							sleep(sleepinterval / 1000000L);
						} catch (InterruptedException e) {
							logger.log(Level.SEVERE, "No wait possible! {0}", e.getMessage());
						}
					}
				}
			}
		}
		// indicate that schedule has stopped
		this.is_stopped = true;
	}
	/***************************************************************************
	 * 
	 * end region run function of thread object
	 * 
	 **************************************************************************/
}
