/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jacek Pospychala <jacek.pospychala@pl.ibm.com> - bug 202583
 *******************************************************************************/
package com.bichler.astudio.device.opcua;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public final class DeviceSharedImages {

	private DeviceSharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	public final static String ICONS_48_PATH = "img_48/";
	
	
	public static final String ICON_SERVER_START = ICONS_PATH + ICONS_48_PATH + "simulation_start.png"; //$NON-NLS-1$
	public static final String ICON_SERVER_STOP = ICONS_PATH + ICONS_48_PATH + "simulation_stop.png"; //$NON-NLS-1$
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return DeviceActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return DeviceActivator.getDefault().getImageRegistry().get(key);
	}

}
