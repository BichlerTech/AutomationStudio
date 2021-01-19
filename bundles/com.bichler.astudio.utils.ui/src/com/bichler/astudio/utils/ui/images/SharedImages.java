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
package com.bichler.astudio.utils.ui.images;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.utils.ui.UtilsUIActivator;

public final class SharedImages {

	private SharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	
	/** */
	public static final String ICON_CHECKED_0 = ICONS_PATH + "checked_0.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_0_GRAY = ICONS_PATH + "checked_0_gray.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1 = ICONS_PATH + "checked_1.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1_GRAY = ICONS_PATH + "checked_1_gray.png"; //$NON-NLS-1$
	public static final String ICON_LOOK = ICONS_PATH + "look.png"; //$NON-NLS-1$
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return UtilsUIActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return UtilsUIActivator.getDefault().getImageRegistry().get(key);
	}

}
