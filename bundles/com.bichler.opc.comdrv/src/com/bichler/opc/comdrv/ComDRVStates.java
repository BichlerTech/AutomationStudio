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

/**
 * this class contains all driver state constants
 *
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2009
 * 
 */
public class ComDRVStates {
	/**
	 * driver is normally running
	 */
	public static final long RUNNING = 1;
	/**
	 * driver is stopped and need to be started
	 */
	public static final long STOPPED = 2;
	/**
	 * driver is starting so no address space for that driver is reachable
	 */
	public static final long STARTING = 3;
	/**
	 * driver is waiting for response or activation
	 */
	public static final long WAITING = 10;
	/**
	 * driver is redundancy and wait for start flag when main driver suspense
	 */
	public static final long REDUNDANCY = 50;
}
