/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: IBM Corporation - initial API and implementation Jacek
 * Pospychala <jacek.pospychala@pl.ibm.com> - bug 202583
 *******************************************************************************/
package com.bichler.astudio.editor.xml_da;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public final class XML_DA_SharedImages
{
  private XML_DA_SharedImages()
  { // do nothing
  }
  public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
  public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
  public final static String ICONS_24_PATH = "img_24/"; //$NON-NLS-1$
  public static final String ICON_CHECKED_0 = ICONS_PATH + ICONS_16_PATH + "checked_0.png"; //$NON-NLS-1$
  public static final String ICON_CHECKED_0_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_0_gray.png"; //$NON-NLS-1$
  public static final String ICON_CHECKED_1 = ICONS_PATH + ICONS_16_PATH + "checked_1.png"; //$NON-NLS-1$
  public static final String ICON_CHECKED_1_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_1_gray.png"; //$NON-NLS-1$
  public static final String ICON_DATEPOINT = ICONS_PATH + ICONS_16_PATH + "dp.png"; //$NON-NLS-1$
  public static final String ICON_DATEPOINTLEAF = ICONS_PATH + ICONS_16_PATH + "dpleaf.png"; //$NON-NLS-1$
  // public static final String ICON_ADD = ICONS_PATH + ICONS_24_PATH +
  // "add.png"; //$NON-NLS-1$
  public static final String ICON_ADD = ICONS_PATH + ICONS_24_PATH + "add.png"; //$NON-NLS-1$
  public static final String ICON_DELETE = ICONS_PATH + ICONS_24_PATH + "delete.png"; //$NON-NLS-1$
  public static final String ICON_IMPORT = ICONS_PATH + ICONS_24_PATH + "import.png"; //$NON-NLS-1$
  public static final String ICON_TEST = ICONS_PATH + ICONS_24_PATH + "test.png"; //$NON-NLS-1$

  public static ImageDescriptor getImageDescriptor(String key)
  {
    return XML_DA_Activator.getDefault().getImageRegistry().getDescriptor(key);
  }

  public static Image getImage(String key)
  {
    return XML_DA_Activator.getDefault().getImageRegistry().get(key);
  }
}
