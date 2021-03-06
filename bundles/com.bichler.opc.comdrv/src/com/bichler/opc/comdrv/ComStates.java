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
 * this class contains all general state constants
 *
 * @version v1.0.0.1
 * @author hannes
 * @since 01.10.2009
 * 
 */
public class ComStates {
	/**
	 * indicate that no error happens after any action
	 */
	public static final long OK = 0;
	/**
	 * driver doesn't exists
	 */
	public static final long DRVNOTFOUND = -1;
	/**
	 * data points list couldn't be found
	 */
	public static final long NODPLISTFOUND = -101;
	/**
	 * data point couldn't be found
	 */
	public static final long DPNOTFOUND = -100;
	/**
	 * communication object doesn't exist
	 */
	public static final long COMNOTFOUND = -1000;
	/**
	 * communication object doesn't exist
	 */
	public static final long COMCLOSED = -1001;
}
