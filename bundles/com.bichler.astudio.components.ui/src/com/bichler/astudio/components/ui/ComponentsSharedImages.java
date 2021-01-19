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
package com.bichler.astudio.components.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public final class ComponentsSharedImages {

	private ComponentsSharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	
	public final static String ICON_COMET = ICONS_PATH + ICONS_PATH + "comet.png";
	
	public static final String ICON_DATATYPE = ICONS_PATH + ICONS_16_PATH + "datatype.png"; //$NON-NLS-1$
	public static final String ICON_FOLDER = ICONS_PATH + ICONS_16_PATH + "folder.png"; //$NON-NLS-1$
	public static final String ICON_METHOD_M = ICONS_PATH + ICONS_16_PATH + "method_m.png"; //$NON-NLS-1$
	public static final String ICON_METHOD_O = ICONS_PATH + ICONS_16_PATH + "method_o.png"; //$NON-NLS-1$
	public static final String ICON_METHOD = ICONS_PATH + ICONS_16_PATH + "method.png"; //$NON-NLS-1$
	public static final String ICON_OBJECT_M = ICONS_PATH + ICONS_16_PATH + "object_m.png"; //$NON-NLS-1$
	public static final String ICON_OBJECT_O = ICONS_PATH + ICONS_16_PATH + "object_o.png"; //$NON-NLS-1$
	public static final String ICON_OBJECT = ICONS_PATH + ICONS_16_PATH + "object.png"; //$NON-NLS-1$
	public static final String ICON_OBJECTTYPE = ICONS_PATH + ICONS_16_PATH + "objecttype.png"; //$NON-NLS-1$
	public static final String ICON_REFERENCETYPE = ICONS_PATH + ICONS_16_PATH + "referencetype.png"; //$NON-NLS-1$
	public static final String ICON_VARIABLE_M = ICONS_PATH + ICONS_16_PATH + "variable_m.png"; //$NON-NLS-1$
	public static final String ICON_VARIABLE_O = ICONS_PATH + ICONS_16_PATH + "variable_o.png"; //$NON-NLS-1$
	public static final String ICON_VARIABLE = ICONS_PATH + ICONS_16_PATH + "variable.png"; //$NON-NLS-1$
	public static final String ICON_VARIABLETYPE = ICONS_PATH + ICONS_16_PATH + "variabletype.png"; //$NON-NLS-1$
	public static final String ICON_VIEW = ICONS_PATH + ICONS_16_PATH + "view.png"; //$NON-NLS-1$
	public static final String ICON_EVENT = ICONS_PATH + ICONS_16_PATH + "event.png"; //$NON-NLS-1$
	public static final String ICON_ALARM = ICONS_PATH + ICONS_16_PATH + "alarm.png";
	public static final String ICON_HOSTCONNECTION = ICONS_PATH + ICONS_16_PATH + "hostconnection.png";
	public static final String ICON_HOSTCONNECTION_GREEN = ICONS_PATH + ICONS_16_PATH + "hostconnection_green.png";
	
	public static final String ICONS_SECURITY_CERT = ICONS_PATH + ICONS_16_PATH + "stock_certificate.png";
	public static final String ICON_SERVER_UA = ICONS_PATH + ICONS_16_PATH + "server.png";
	public static final String ICON_DISCOVERY_UA = ICONS_PATH + ICONS_16_PATH + "discovery_server.png";
	
	public static final String ICON_ENDPOINT_UA = ICONS_PATH + ICONS_16_PATH + "endpoint.gif";
	public static final String ICON_SECURITY_UA = ICONS_PATH + ICONS_16_PATH + "security.png";
	public static final String ICON_SECURITY_NONE = ICONS_PATH + ICONS_16_PATH + "security_none.png";

	public static final String ICON_SCRIPTS = ICONS_PATH + ICONS_16_PATH + "security_none.png";
	
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return ComponentsUIActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return ComponentsUIActivator.getDefault().getImageRegistry().get(key);
	}

}
