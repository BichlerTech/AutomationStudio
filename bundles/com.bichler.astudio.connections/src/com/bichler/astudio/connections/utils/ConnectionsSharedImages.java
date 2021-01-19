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
package com.bichler.astudio.connections.utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.connections.ConnectionsActivator;

public final class ConnectionsSharedImages {

	private ConnectionsSharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String WIZARDS_PATH = "wizards/";
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	public final static String ICONS_32_PATH = "img_32/"; //$NON-NLS-1$

	public static final String ICON_HOSTCONNECTION = ICONS_PATH + ICONS_16_PATH + "hostconnection.png"; //$NON-NLS-1$
	public static final String ICON_HOSTCONNECTION_GREEN = ICONS_PATH + ICONS_16_PATH + "hostconnection_green.png"; //$NON-NLS-1$
	
	public static final String ICON_WIZARD_PROJECT_ADD = ICONS_PATH + WIZARDS_PATH + "project_wizard_banner.png"; //$NON-NLS-1$
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return ConnectionsActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return ConnectionsActivator.getDefault().getImageRegistry().get(key);
	}

}
