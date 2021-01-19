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
package com.bichler.astudio.editor.aggregated;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public final class AggregatedSharedImages {

	private AggregatedSharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	public final static String ICONS_24_PATH = "img_24/"; //$NON-NLS-1$
	public final static String ICONS_32_PATH = "img_32/"; //$NON-NLS-1$
	public final static String BUTTONS_PATH = "buttons/";
	
	public final static String BROWSER_ICONS = "browser_icons/";
	
	public static final String ICON_CHECKED_0 = ICONS_PATH + ICONS_16_PATH + "checked_0.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_0_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_0_gray.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1 = ICONS_PATH + ICONS_16_PATH + "checked_1.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_1_gray.png"; //$NON-NLS-1$
	
//	public static final String ICON_ADD = ICONS_PATH + ICONS_24_PATH + "add.png"; //$NON-NLS-1$
	public static final String ICON_DELETE = ICONS_PATH + ICONS_24_PATH + "delete.png"; //$NON-NLS-1$
	public static final String ICON_IMPORT = ICONS_PATH + ICONS_24_PATH + "import.png"; //$NON-NLS-1$
	
	public static final String ICONS_DATATYPE_IMG = ICONS_PATH + BROWSER_ICONS + "datatype_16.png";
	public static final String ICONS_METHOD_IMG = ICONS_PATH + BROWSER_ICONS + "method_16.png";
	public static final String ICONS_OBJECT_IMG = ICONS_PATH + BROWSER_ICONS + "object_16.png";
	public static final String ICONS_FOLDER_IMG = ICONS_PATH + BROWSER_ICONS + "folder_16.png";
	public static final String ICONS_OBJECTTYPE_IMG = ICONS_PATH + BROWSER_ICONS + "objecttype_16.png";
	public static final String ICONS_REFERENCETYPE_IMG = ICONS_PATH + BROWSER_ICONS + "referencetype_16.png";
	public static final String ICONS_VARIABLETYPE_IMG = ICONS_PATH + BROWSER_ICONS + "variabletype_16.png";
	public static final String ICONS_VARIABLE_IMG = ICONS_PATH + BROWSER_ICONS + "variable_16.png";
	public static final String ICONS_VIEW_IMG = ICONS_PATH + BROWSER_ICONS + "view_16.png";
	public static final String ICONS_EVENT_IMG = ICONS_PATH + BROWSER_ICONS + "event_16.png";
	public static final String ICONS_ALARM_IMG = ICONS_PATH + BROWSER_ICONS + "alarm_16.png";
	
	public static final String ICON_BUTTON_SERVER_STOPPED = ICONS_PATH + BUTTONS_PATH + "stopped.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_RUNNING = ICONS_PATH + BUTTONS_PATH + "started.png"; //$NON-NLS-1$
	
	public static final String ICON_BUTTON_SERVER_START = ICONS_PATH + BUTTONS_PATH + "start.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_START_SELECTED = ICONS_PATH + BUTTONS_PATH + "start_selected.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_START_DISABLED = ICONS_PATH + BUTTONS_PATH + "start_disabled.png"; //$NON-NLS-1$
	
	public static final String ICON_BUTTON_SERVER_STOP = ICONS_PATH + BUTTONS_PATH + "stop.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_STOP_SELECTED = ICONS_PATH + BUTTONS_PATH + "stop_selected.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_STOP_DISABLED = ICONS_PATH + BUTTONS_PATH + "stop_disabled.png"; //$NON-NLS-1$
	
	public static final String ICON_CHECK = ICONS_PATH + ICONS_32_PATH + "select_check.png"; //$NON-NLS-1$
	public static final String ICON_BROWSE = ICONS_PATH + ICONS_32_PATH + "browse.png"; //$NON-NLS-1$
	public static final String ICON_CONNECT = ICONS_PATH + ICONS_32_PATH + "connect.png"; //$NON-NLS-1$
	public static final String ICON_OPEN = ICONS_PATH + ICONS_32_PATH + "open.png"; //$NON-NLS-1$
	public static final String ICON_CLOSE = ICONS_PATH + ICONS_32_PATH + "close.png"; //$NON-NLS-1$
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return AggregatedActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return AggregatedActivator.getDefault().getImageRegistry().get(key);
	}

}
