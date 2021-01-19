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

import java.io.FileNotFoundException;

/**
 * 
 * @author hannes bichler
 * @version 1.00.1
 *
 */
public class ComDriverNotFoundExeption extends FileNotFoundException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4179725898602358584L;

	public ComDriverNotFoundExeption(String drvName, String path) {
		super("Driver - " + drvName + " - not found on path - " + path + " - !");
	}

	public ComDriverNotFoundExeption(String drvName) {
		super("Driver - " + drvName + " - not found because of unkown path - !");
	}
}
