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
 * this class contains all data point state constants
 *
 * @version v1.0.0.1
 * @author hannes
 * @since 01.10.2009
 * 
 */
public class ComDPStates {
	/**
	 * data point is in inial state (no read/write)
	 */
	public static final long INITIAL = 100;
	/**
	 * data point value is changed after notification
	 */
	public static final long NOTIFICATIONCHANGED = 101;
	/**
	 * data point value is changed after write from visu
	 */
	public static final long WRITEFROMVISU = 110;
	/**
	 * data point value is changed after read from visu
	 */
	public static final long READFROMVISU = 111;
	/**
	 * data point value is changed after read from visu
	 */
	public static final long WRITEFROMDRV = 112;
	/**
	 * data point value is changed after write from bridge
	 */
	public static final long WRITEFROMBRIDGE = 120;
	/**
	 * data point value is changed after read from bridge
	 */
	public static final long READFROMBRIDGE = 121;
	/**
	 * data point value is changed after write from trendchart
	 */
	public static final long WRITEFROMTRENDCHART = 130;
	/**
	 * data point value is changed after read from trendchart
	 */
	public static final long READFROMTRENDCHART = 131;
	/**
	 * data point value is changed after read from scheduler
	 */
	public static final long READFROMSCHEDULER = 140;
	/**
	 * data point value could not be changed by scheduler
	 */
	public static final long READFROMSCHEDULERFAILURE = 141;
	/**
	 * data point value is changed after read from storage
	 */
	public static final long READFROMSTORAGE = 150;
	/**
	 * data point value is changed after write from opcua
	 */
	public static final long WRITEFROMUA = 200;

	private ComDPStates() {
	}
}
