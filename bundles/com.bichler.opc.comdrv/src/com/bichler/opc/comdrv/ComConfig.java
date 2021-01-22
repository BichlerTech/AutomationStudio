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
/**
 * @version v1.0.0.1
 * @since 05.05.2009
 * 
 * @company HB-Softsolution e.U.
 * @web www.hb-softsoluiton.com
 * 
 * @author hannes bichler
 * @contact hannes.bichler@hb-softsolution.com
 * 
 *          description: this class contains all configured variables
 * 
 *          ---------------------------------------------------------------
 */
package com.bichler.opc.comdrv;

/**
 * this class contains all configured variables
 * 
 * @author hannes
 *
 */
public class ComConfig {
	/**
	 * interval for schedule loop in milliseconds
	 */
	public static final long SCHEDULEINTERVAL = 50L * 1000000;

	private ComConfig() {
	}
}