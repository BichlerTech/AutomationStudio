// (c) Copyright 2010 HB-Softsolution e.U.
// ALL RIGHTS RESERVED.
//
// DISCLAIMER:
// This code is provided and developed by HB-Softsolution e.U.
// Distribution of this source code underlies the Warranty
// and Liability Disclaimers which appear in the
// HB-Softsolution e.U. license agreements.
//
// Author: hannes bichler
// Company: HB-Softsolution e.U.
// Web: www.hb-softsoluiton.com
// contact: hannes.bichler@hb-softsolution.com
package com.bichler.opc.comdrv;

/**
 * This class contains all communication state constants.
 *
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2010
 * 
 */
public class ComCommunicationStates {
	/**
	 * communication is closed
	 */
	public static final long CLOSED = 1000;
	/**
	 * communication is open
	 */
	public static final long OPEN = 1001;

	private ComCommunicationStates() {
	}
}
